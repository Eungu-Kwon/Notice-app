package com.eungu.notice.Home_Menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eungu.notice.Extra.ComputeClass;
import com.eungu.notice.Extra.SettingDataHelper;
import com.eungu.notice.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import at.markushi.ui.CircleButton;

public class HomeFragment extends Fragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.home_activity, container, false);
        final CircleButton b = v.findViewById(R.id.main_service_btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("buttonTag", "button pressed");
                ComputeClass cp = new ComputeClass();
                Intent noti_service = new Intent(v.getContext(), NotiService.class);
                if(!cp.isLaunchingService(v.getContext())){
                    Log.i("button_tag", "start_svc");
                    b.setColor(Color.parseColor("#99CC00"));
                    b.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_alarm_on_foreground));

                    if(Build.VERSION.SDK_INT >= 26)
                        getContext().startForegroundService(noti_service);
                    else
                        getContext().startService(noti_service);
                }
                else {
                    Log.i("button_tag", "stop_svc");
                    b.setColor(Color.parseColor("#EC3743"));
                    b.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_alarm_off_foreground));
                    getContext().stopService(noti_service);
                    SharedPreferences setting = getContext().getSharedPreferences(SettingDataHelper.PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putBoolean("isRunning", false);
                    editor.commit();
                }
            }
        });

        final EasyFlipView card = v.findViewById(R.id.main_card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.flipTheView();
            }
        });

        Button setting = v.findViewById(R.id.card_btn1);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting_intent = new Intent(v.getContext(), NotiServiceSetting.class);
                v.getContext().startActivity(setting_intent);
            }
        });

        if(!ComputeClass.isLaunchingService(getContext())) {
            b.setColor(Color.parseColor("#EC3743"));
            b.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_alarm_off_foreground));
        }
        else {
            b.setColor(Color.parseColor("#99CC00"));
            b.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_alarm_on_foreground));
        }

        Log.i("myLifeCycle", "onCreateView()");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        SettingDataHelper dataHelper = new SettingDataHelper(v.getContext());

        TextView card_title = v.findViewById(R.id.card_title);
        TextView card_content = v.findViewById(R.id.card_content);
        TextView card_rear_title = v.findViewById(R.id.card_title_rear);
        card_title.setText(dataHelper.getStringData(SettingDataHelper.MAIN_TITLE, "알림제목"));
        card_content.setText(dataHelper.getStringData(SettingDataHelper.MAIN_CONTENT,"탭해서 문구설정"));

        switch (Integer.parseInt(dataHelper.getStringData(SettingDataHelper.MAIN_CATEGORY, "0"))){
            case 0:
                card_rear_title.setText("탭해서 뒤집기");
                break;
            case 1:
                card_rear_title.setText("URL 이동");
                break;
            case 2:
                String card_app = dataHelper.getStringData(SettingDataHelper.APPNAME, "");
                card_app = card_app.replace("\n", " ");
                if(card_app.length() > 7){
                    card_app = card_app.substring(0, 7) + "...";
                }
                card_rear_title.setText(card_app + " 실행");
                break;
            case 3:
                String card_person = dataHelper.getStringData(SettingDataHelper.PNAME, "");
                if(card_person.length() > 6){
                    card_person = card_person.substring(0, 6) + "... ";
                }
                card_rear_title.setText(card_person + "님께 전화");
                break;
        }

    }
}
