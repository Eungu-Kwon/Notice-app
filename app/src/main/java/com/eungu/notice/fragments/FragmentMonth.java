package com.eungu.notice.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.DaySquareButton;
import com.eungu.notice.R;

import java.util.Calendar;

public class FragmentMonth extends Fragment {

    private DateSetListener onDataSetListener = null;
    Calendar time = null;
    DaySquareButton[] month_btn;
    int days = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_alarm_month, container, false);
        LinearLayout fr = view.findViewById(R.id.frame_month);

        time = Calendar.getInstance();
        time.set(Calendar.SECOND, 0);
        month_btn = new DaySquareButton[35];
        LinearLayout[] ll = new LinearLayout[5];

        for(int i = 0; i < 5; i++){
            ll[i] = new LinearLayout(getActivity());
            ll[i].setOrientation(LinearLayout.HORIZONTAL);
            ll[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            for(int j = 0; j < 7; j++){
                final int idx = i*7+j;
                month_btn[idx] = new DaySquareButton(getActivity());
                month_btn[idx].setBackgroundResource(android.R.color.transparent);
                LinearLayout.LayoutParams bottomButtonParams = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                bottomButtonParams.weight = 1;
                month_btn[idx].setLayoutParams(bottomButtonParams);
                if(idx < 31){
                    month_btn[idx].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            days ^= (1 << idx);
                            onDataSetListener.setData(time, days, DBData.RING_MONTH, DBData.CONTENT_NORMAL);
                            setBtnIcon(idx);
                            set_btn_text();
                        }
                    });

                    month_btn[idx].setText(idx+1 + "");
                }
                ll[i].addView(month_btn[idx]);
            }
            fr.addView(ll[i]);
        }

        TimePicker picker = new TimePicker(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar));
        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);
                onDataSetListener.setData(time, days, DBData.RING_MONTH, DBData.CONTENT_NORMAL);
            }
        });
        fr.addView(picker);

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

    void setBtnIcon(int idx){
        if((days & (1 << idx)) > 0)
            month_btn[idx].setBackgroundResource(R.drawable.circle_xml);
        else month_btn[idx].setBackgroundResource(android.R.color.transparent);
    }

    void set_btn_text(){
        if(days == 0){
            onDataSetListener.setBtnText("날짜를 선택해주세요");
        }
        String str = "매월 ";
        boolean isFirst = true;
        for(int i = 0; i < 31; i++){
            if((days & (1 << i)) > 0){
                if(isFirst){
                    isFirst = false;
                }
                else str += ", ";
                str += (i+1);
            }
        }
        str += "일";
        onDataSetListener.setBtnText(str);
    }
}
