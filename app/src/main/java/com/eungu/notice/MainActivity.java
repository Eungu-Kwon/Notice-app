package com.eungu.notice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.eungu.notice.List_Menu.ListFragment;
import com.eungu.notice.R;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "SettingFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_frame);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment fragment = new ListFragment();

        fragmentTransaction.add(R.id.main_frame, fragment).commit();
    }
}
