package com.eungu.notice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.eungu.notice.DBManager.AlarmDBHelper;
import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.list_maker.AlarmListAdapter;
import com.eungu.notice.list_maker.AlarmListItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<AlarmListItem> list = null;
    AlarmListAdapter listAdapter = null;
    boolean isServiceRunning = false, isDeleting = false;
    RecyclerView recyclerView;
    Switch main_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton add_btn = findViewById(R.id.add_btn);
        ImageButton delete_btn = findViewById(R.id.l_delete);
        final Button delete_done = findViewById(R.id.delete_done_btn);
        delete_done.setVisibility(View.GONE);
        init_list();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlarmSettingActivity.class);
                intent.putExtra("isNew", true);
                startActivity(intent);
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDeleting) {
                    delete_done.setVisibility(View.VISIBLE);
                    isDeleting = true;
                    listAdapter.setDeleteMode(true);
                }
                else{
                    delete_done.setVisibility(View.GONE);
                    isDeleting = false;
                    listAdapter.setDeleteMode(false);
                }
                listAdapter.notifyDataSetChanged();
            }
        });
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
                isDeleting = false;
                listAdapter.setDeleteMode(false);

                listAdapter.notifyDataSetChanged();
            }
        });
        main_sw_init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    void main_sw_init(){
        main_switch = (Switch)findViewById(R.id.main_switch);
        isServiceRunning = isLaunchingService(getApplicationContext());
        main_switch.setChecked(isServiceRunning);
        listAdapter.setEnable(isServiceRunning);
        main_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent main_service = new Intent(getApplicationContext(), NotiService.class);
                if(isChecked) {
                    startService(main_service);
                }
                else {
                    //TODO make dialog
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

            list.add(item);
        }
        listAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("deprecation")
    public static Boolean isLaunchingService(Context mContext){

        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotiService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return  false;
    }
}
