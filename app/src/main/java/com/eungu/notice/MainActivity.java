package com.eungu.notice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.eungu.notice.list_maker.AlarmListAdapter;
import com.eungu.notice.list_maker.AlarmListItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<AlarmListItem> list = null;
    AlarmListAdapter listAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button setting_btn = findViewById(R.id.setting_btn);
        Switch main_switch = (Switch)findViewById(R.id.main_switch);

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO make setting
                Intent intent = new Intent(getApplicationContext(), AlarmSettingActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "setting button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        main_switch.setChecked(isLaunchingService(getApplicationContext()));

        main_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent main_service = new Intent(getApplicationContext(), NotiService.class);
                if(isChecked)
                    startService(main_service);
                else
                    //TODO make dialog
                    stopService(main_service);
            }
        });
        setList();

        list = new ArrayList<>();
        AlarmListItem item = new AlarmListItem();
        item.setTitle( "번째");
        list.add(item);
        RecyclerView recyclerView = findViewById(R.id.alarmlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listAdapter = new AlarmListAdapter(list);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    void setList(){
        for(int i = 1; i <= 17; i++){
            AlarmListItem item = new AlarmListItem();
            item.setTitle(i + "번째");
            list.add(item);
        }

        listAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("deprecation")
    public Boolean isLaunchingService(Context mContext){

        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotiService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return  false;
    }
}
