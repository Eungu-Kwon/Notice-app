package com.eungu.notice.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.eungu.notice.R;

public class FragmentMonth extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_alarm_month, container, false);
        LinearLayout fr = view.findViewById(R.id.frame_month);

        Button[] month_btn = new Button[31];

        for(int i = 0; i < 31; i++){
            month_btn[i] = new Button(getActivity());
            LinearLayout.LayoutParams bottomButtonParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            month_btn[i].setLayoutParams(bottomButtonParams);
            month_btn[i].setText(i + "");
            fr.addView(month_btn[i]);
        }
        return view;
    }
}
