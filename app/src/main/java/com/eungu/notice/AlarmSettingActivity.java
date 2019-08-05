package com.eungu.notice;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eungu.notice.datapicker.AlarmTimePicker;

import java.util.Calendar;

public class AlarmSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_alarm);

        makePickerDialog();

        Button set_condition_btn = findViewById(R.id.set_condition_btn);
        set_condition_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] oItem = {"바로 띄우기", "날짜 선택", "요일별", "월별"};

                AlertDialog.Builder oDialog = new AlertDialog.Builder(AlarmSettingActivity.this);
                oDialog.setTitle("조건을 선택하세요")
                        .setItems(oItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), oItem[which], Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = oDialog.create();
                alertDialog.show();
            }
        });
    }

    @SuppressWarnings("deprecation")
    void makePickerDialog(){
        final EditText timeIp = findViewById(R.id.time_input);
        final EditText dateIp = findViewById(R.id.date_input);
        timeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int t_hour = c.get(Calendar.HOUR_OF_DAY);
                final int t_minute = c.get(Calendar.MINUTE);
                TimePickerDialog tp;
                tp = new TimePickerDialog(AlarmSettingActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeIp.setText(hourOfDay + " : " + minute);
                    }
                }, t_hour, t_minute, DateFormat.is24HourFormat(AlarmSettingActivity.this));
                tp.setTitle("SetTime");
                tp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                tp.show();
            }
        });
        dateIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int t_year = c.get(Calendar.YEAR);
                int t_month = c.get(Calendar.MONTH);
                int t_day = c.get(Calendar.DATE);
                DatePickerDialog dp = new DatePickerDialog(AlarmSettingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month+=1;
                        dateIp.setText(year + " / " + month + " / " + dayOfMonth);
                    }
                }, t_year, t_month, t_day);
                dp.setTitle("Set Day");
                dp.show();
            }
        });
    }
}