package com.eungu.notice.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eungu.notice.AlarmSettingActivity;
import com.eungu.notice.R;

import java.util.Calendar;

public class FragmentOnce extends Fragment {

    private DateSetListener onDataSetListener = null;
    Calendar time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_alarm_once, container, false);
        makePickerDialog(view);
        time = Calendar.getInstance();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onDataSetListener = (DateSetListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDataSetListener = null;
    }

    @SuppressWarnings("deprecation")
    void makePickerDialog(View view){
        final CheckBox cb = view.findViewById(R.id.now_check);
        final EditText timeIp = view.findViewById(R.id.time_input);
        final EditText dateIp = view.findViewById(R.id.date_input);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    timeIp.setEnabled(false);
                    dateIp.setEnabled(false);
                }
                else{
                    timeIp.setEnabled(true);
                    dateIp.setEnabled(true);
                }
            }
        });
        timeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int t_hour = time.get(Calendar.HOUR_OF_DAY);
                final int t_minute = time.get(Calendar.MINUTE);
                TimePickerDialog tp;
                tp = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeIp.setText(hourOfDay + "시 " + minute + "분");
                        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        time.set(Calendar.MINUTE, minute);
                        Toast.makeText(getContext(), "T" + dateIp.getText(), Toast.LENGTH_SHORT).show();
                    }
                }, t_hour, t_minute, DateFormat.is24HourFormat(getActivity()));
                tp.setTitle("시간 선택");
                tp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                tp.show();
            }
        });
        dateIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int t_year = time.get(Calendar.YEAR);
                int t_month = time.get(Calendar.MONTH);
                int t_day = time.get(Calendar.DATE);
                DatePickerDialog dp = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateIp.setText(year + "년 " + (month+1) + "월 " + dayOfMonth + "일");
                        time.set(Calendar.YEAR, year);
                        time.set(Calendar.MONTH, month);
                        time.set(Calendar.DATE, dayOfMonth);
                    }
                }, t_year, t_month, t_day);
                dp.setTitle("날짜 선택");
                dp.show();
            }
        });
    }
}