package com.eungu.notice.List_Menu.fragments;

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

import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.R;

import java.util.Calendar;

public class FragmentOnce extends Fragment {

    private DateSetListener onDataSetListener = null;
    Calendar time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_alarm_once, container, false);

        if(getArguments() == null){
            time = Calendar.getInstance();
            time.set(Calendar.SECOND, 0);
            time.set(Calendar.MILLISECOND, 0);
        }
        else{
            String arg_time = getArguments().getString("time", "none");
            DBData d = new DBData();
            d.setTimeFromText(arg_time);
            time = d.getTime();
            setText(time, view);
        }

        makePickerDialog(view);
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
    void makePickerDialog(final View mView){
        final CheckBox cb = mView.findViewById(R.id.now_check);
        final EditText timeIp = mView.findViewById(R.id.time_input);
        final EditText dateIp = mView.findViewById(R.id.date_input);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Calendar tempC = Calendar.getInstance();
                    tempC.set(Calendar.SECOND, 0);

                    timeIp.setEnabled(false);
                    dateIp.setEnabled(false);
                    setText(tempC, mView);

                    time.setTime(tempC.getTime());
                    onDataSetListener.setData(null, 1, DBData.RING_ONCE, DBData.CONTENT_NORMAL);
                }
                else{
                    timeIp.setEnabled(true);
                    dateIp.setEnabled(true);
                    timeIp.setText("");
                    dateIp.setText("");
                    onDataSetListener.setData(null, -1, DBData.RING_ONCE, DBData.CONTENT_NORMAL);
                }
            }
        });
        timeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int t_hour = time.get(Calendar.HOUR_OF_DAY);
                final int t_minute = time.get(Calendar.MINUTE);
                TimePickerDialog tp;
                tp = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        time.set(Calendar.MINUTE, minute);
                        setText(time, mView);
                        onDataSetListener.setData(time, -1, DBData.RING_ONCE, DBData.CONTENT_NORMAL);
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
                        time.set(Calendar.YEAR, year);
                        time.set(Calendar.MONTH, month);
                        time.set(Calendar.DATE, dayOfMonth);
                        setText(time, mView);
                        onDataSetListener.setData(time, -1, DBData.RING_ONCE, DBData.CONTENT_NORMAL);
                    }
                }, t_year, t_month, t_day);
                dp.setTitle("날짜 선택");
                dp.show();
            }
        });
    }

    void setText(Calendar c, View view){
        final EditText timeIput = view.findViewById(R.id.time_input);
        final EditText dateIput = view.findViewById(R.id.date_input);

        int t_hour = c.get(Calendar.HOUR_OF_DAY);
        int t_minute = c.get(Calendar.MINUTE);
        dateIput.setText(c.get(Calendar.YEAR) + "년 " + (c.get(Calendar.MONTH)+1) + "월 "+ c.get(Calendar.DAY_OF_MONTH) + "일");
        if(t_hour < 12) timeIput.setText("오전 " + t_hour + "시 "+ t_minute + "분");
        else {
            if(t_hour == 12) timeIput.setText("오후 " + 12 + "시 "+ t_minute + "분");
            else timeIput.setText("오후 " + (t_hour - 12) + "시 "+ t_minute + "분");
        }
    }
}