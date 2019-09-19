package com.eungu.notice.Home_Menu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eungu.notice.Extra.SettingDataHelper;
import com.eungu.notice.List_Menu.NotiService;
import com.eungu.notice.R;

public class NotiServiceSetting extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti_service_setting);
        Button done_btn = findViewById(R.id.noti_done);
        final EditText title = findViewById(R.id.noti_title_edit);
        final EditText content = findViewById(R.id.noti_content_edit);

        final SettingDataHelper dataHelper = new SettingDataHelper(getApplicationContext());

        title.setText(dataHelper.getStringData(SettingDataHelper.MAIN_TITLE, ""));
        content.setText(dataHelper.getStringData(SettingDataHelper.MAIN_CONTENT, ""));

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataHelper.settingStringData(SettingDataHelper.MAIN_TITLE, title.getText().toString());
                dataHelper.settingStringData(SettingDataHelper.MAIN_CONTENT, content.getText().toString());

                Intent service = new Intent(v.getContext(), NotiService.class);
                stopService(service);
                if(Build.VERSION.SDK_INT >= 26)
                    v.getContext().startForegroundService(service);
                else
                    v.getContext().startService(service);

                finish();
            }
        });
    }
}
