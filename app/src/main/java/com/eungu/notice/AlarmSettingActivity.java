package com.eungu.notice;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.eungu.notice.datapicker.AlarmTimePicker;

import java.util.Calendar;

public class AlarmSettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_alarm);

        final EditText timeIp = findViewById(R.id.time_input);
        timeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                final int miniute = c.get(Calendar.MINUTE);
                TimePickerDialog tp;
                tp = new TimePickerDialog(AlarmSettingActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeIp.setText(hourOfDay + " : " + miniute);
                    }
                }, hour, miniute, DateFormat.is24HourFormat(AlarmSettingActivity.this));
                tp.setTitle("SetTime");
                tp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                tp.show();
            }
        });

        Button done_btn = findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
