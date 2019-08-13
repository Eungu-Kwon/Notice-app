package com.eungu.notice.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.R;

import java.util.Calendar;

public class FragmentWeekOfDay extends Fragment{

    private DateSetListener onDataSetListener = null;
    Calendar time = null;
    private int days;
    Button[] days_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_alarm_week_of_day, container, false);
        time = Calendar.getInstance();
        time.set(Calendar.SECOND, 0);
        setDays(view);
        setTime(view);
        return view;
    }

    public void setDays(View view){
        attachButtons(view);
    }

    public void attachButtons(View view){
        days_btn = new Button[7];
        days_btn[0] = view.findViewById(R.id.day_mon_btn);
        days_btn[1] = view.findViewById(R.id.day_tue_btn);
        days_btn[2] = view.findViewById(R.id.day_wed_btn);
        days_btn[3] = view.findViewById(R.id.day_thu_btn);
        days_btn[4] = view.findViewById(R.id.day_fri_btn);
        days_btn[5] = view.findViewById(R.id.day_sat_btn);
        days_btn[6] = view.findViewById(R.id.day_sun_btn);



        for(int i = 0; i < 7; i++){
            final int idx = i;
            days_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    days ^= (1 << idx);
                    onDataSetListener.setData(time, days, DBData.RING_DAYOFWEEK, DBData.CONTENT_NORMAL);
                    String btn_str = WhichDay(days);
                    onDataSetListener.setBtnText(btn_str);
                    setBtnIcon(idx);
                }
            });
        }
    }

    void setTime(View view){
        TimePicker picker = view.findViewById(R.id.wd_timepicker);
        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                onDataSetListener.setData(time, days, DBData.RING_DAYOFWEEK, DBData.CONTENT_NORMAL);
            }
        });
    }
    void setBtnIcon(int i){
        if((days & 1 << i) > 0){
            days_btn[i].setBackgroundResource(R.drawable.circle_xml);
        }
        else days_btn[i].setBackgroundResource(android.R.color.transparent);
    }

    String WhichDay(int day){
        if(day == 0) return "요일을 선택해주세요";
        String ret = "매주 ";
        boolean isFirst = true;
        if((day & 1 << 0) > 0){
            ret += "월";
            isFirst = false;
        }
        if((day & 1 << 1) > 0){
            if(isFirst){
                isFirst = false;
            }
            else ret += ", ";
            ret += "화";
        }
        if((day & 1 << 2) > 0){
            if(isFirst){
                isFirst = false;
            }
            else ret += ", ";
            ret += "수";
        }
        if((day & 1 << 3) > 0){
            if(isFirst){
                isFirst = false;
            }
            else ret += ", ";
            ret += "목";
        }
        if((day & 1 << 4) > 0){
            if(isFirst){
                isFirst = false;
            }
            else ret += ", ";
            ret += "금";
        }
        if((day & 1 << 5) > 0){
            if(isFirst){
                isFirst = false;
            }
            else ret += ", ";
            ret += "토";
        }
        if((day & 1 << 6) > 0){
            if(isFirst){
                isFirst = false;
            }
            else ret += ", ";
            ret += "일";
        }

        return ret;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        days = 0;
        onDataSetListener = (DateSetListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDataSetListener = null;
    }
}


