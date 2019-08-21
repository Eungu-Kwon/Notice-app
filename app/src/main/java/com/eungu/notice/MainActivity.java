package com.eungu.notice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import com.eungu.notice.DBManager.*;
import com.eungu.notice.Extra.ComputeClass;
import com.eungu.notice.list_maker.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SettingFile";
    final long FINISH_INTERVAL_TIME = 2000;
    long backPressedTime = 0;

    ArrayList<AlarmListItem> list = null;
    AlarmListAdapter listAdapter = null;
    boolean isServiceRunning = false, isDeleting = false;
    RecyclerView recyclerView;
    Switch main_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_setting();
        init_list();

        main_sw_init();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("DataBetweenSA"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void button_setting(){
        final ImageButton setting_btn = findViewById(R.id.setting_btn);
        final Button delete_done = findViewById(R.id.delete_done_btn);
        delete_done.setVisibility(View.GONE);

        delete_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmDBHelper dbHelper = new AlarmDBHelper(getApplicationContext(), "ALARM_TABLE", null, 1);
                boolean[] delete_list = listAdapter.delete_list;
                for(int i = list.size()-1; i >= 0; i--){
                    if(delete_list[i]){
                        dbHelper.deleteColumn(i);
                        list.remove(i);
                    }
                }
                dbHelper.computeID();
                delete_done.setVisibility(View.GONE);
                setting_btn.setVisibility(View.VISIBLE);
                main_switch.setVisibility(View.VISIBLE);
                isDeleting = false;
                listAdapter.setDeleteMode(false);

                listAdapter.notifyDataSetChanged();
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                Menu menu = popup.getMenu();
                inflater.inflate(R.menu.popup_menu, menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.m_item_1:
                                Intent intent = new Intent(getApplicationContext(), AlarmSettingActivity.class);
                                intent.putExtra("isNew", true);
                                startActivity(intent);
                                break;
                            case R.id.m_item_2:
                                if(!isDeleting) {
                                    delete_done.setVisibility(View.VISIBLE);
                                    isDeleting = true;
                                    listAdapter.setDeleteMode(true);
                                    setting_btn.setVisibility(View.INVISIBLE);
                                    main_switch.setVisibility(View.INVISIBLE);
                                }
                                listAdapter.notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    void main_sw_init(){
        main_switch = (Switch)findViewById(R.id.main_switch);

        isServiceRunning = new ComputeClass().isLaunchingService(getApplicationContext(), NotiService.class);
        main_switch.setChecked(isServiceRunning);
        listAdapter.setEnable(isServiceRunning);
        main_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent main_service = new Intent(getApplicationContext(), NotiService.class);
                if(isChecked) {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isRunning", true);
                    editor.commit();
                    if (Build.VERSION.SDK_INT >= 26) {
                        startForegroundService(main_service);
                    }
                    else
                        startService(main_service);
                }
                else {
                    //TODO make dialog
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isRunning", false);
                    editor.commit();
                    stopService(main_service);
                }
                listAdapter.setEnable(isChecked);
                listAdapter.notifyDataSetChanged();
                isServiceRunning = isChecked;
            }
        });
        if(isServiceRunning) {
            Intent main_service = new Intent(getApplicationContext(), NotiService.class);
            stopService(main_service);
            startService(main_service);
        }
    }

    void init_list(){
        list = new ArrayList<>();
        listAdapter = new AlarmListAdapter(getApplicationContext(), list);

        recyclerView = findViewById(R.id.alarmlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    void setList(){
        list.clear();

        AlarmDBHelper dbHelper = new AlarmDBHelper(getApplicationContext(), "ALARM_TABLE", null, 1);
        for(int i = 0; i < dbHelper.getItemsCount(); i++){
            AlarmListItem item = new AlarmListItem();
            DBData dbData = dbHelper.getData(i);
            item.setTitle(dbData.getTitle());
            item.setToggleSw(dbData.isNowEnable()==1);
            item.setTime(dbData.getTimeToText());
            list.add(item);
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if(isDeleting){
            ImageButton setting_btn = findViewById(R.id.setting_btn);
            Button delete_done = findViewById(R.id.delete_done_btn);
            delete_done.setVisibility(View.GONE);
            setting_btn.setVisibility(View.VISIBLE);
            main_switch.setVisibility(View.VISIBLE);
            isDeleting = false;
            listAdapter.setDeleteMode(false);

            listAdapter.notifyDataSetChanged();
        }
        else {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
            {
                super.onBackPressed();
            }
            else
            {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getIntExtra("data", 1) == 1)
                setList();
        }
    };
}
