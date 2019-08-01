package com.eungu.notice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.eungu.notice.datapicker.AlarmTimePicker;

public class AlarmSettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_alarm);
        Button b = findViewById(R.id.time_setting_btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newF = new AlarmTimePicker();
                newF.show(getSupportFragmentManager(), "tag");
            }
        });
    }
}
