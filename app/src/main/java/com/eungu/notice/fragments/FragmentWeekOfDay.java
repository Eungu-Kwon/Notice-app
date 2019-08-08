package com.eungu.notice.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.R;

import java.util.Calendar;

public class FragmentWeekOfDay extends Fragment{

    private DateSetListener onDataSetListener = null;
    private int days;
    Button[] days_btn;
    View.OnClickListener btn_listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_alarm_week_of_day, container, false);
        setDays(view);
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
                    onDataSetListener.setData(Calendar.getInstance(), days, DBData.RING_DAYOFWEEK, DBData.CONTENT_NORMAL);
                }
            });
        }
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
