package com.eungu.notice.Home_Menu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.eungu.notice.Extra.ComputeClass;
import com.eungu.notice.R;

import at.markushi.ui.CircleButton;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_activity, container, false);
        final CircleButton b = v.findViewById(R.id.main_service_btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComputeClass cp = new ComputeClass();
                if(cp.isLaunchingService(v.getContext()){
                    b.setColor(Color.parseColor("#00CCFF"));
                }

            }
        });
        Log.i("myLifeCycle", "onCreateView()");
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("myLifeCycle", "onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("myLifeCycle", "onDestroyView()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("myLifeCycle", "onPause()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("myLifeCycle", "onStart()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("myLifeCycle", "onCreate()");
    }
}
