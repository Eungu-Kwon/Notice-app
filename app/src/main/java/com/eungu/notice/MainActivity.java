package com.eungu.notice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.eungu.notice.Extra.AFragmentListener;
import com.eungu.notice.Home_Menu.HomeFragment;
import com.eungu.notice.List_Menu.ListFragment;
public class MainActivity extends AppCompatActivity implements AFragmentListener {
    final long FINISH_INTERVAL_TIME = 2000;
    long backPressedTime = 0;

    public static final String PREFS_NAME = "SettingFile";
    HomeFragment homeFragment;
    ListFragment fragment;
    boolean listDeleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_frame);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);

        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        homeFragment = new HomeFragment();
        fragment = new ListFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bottom_home:
                        replaceFragment(homeFragment);
                        break;
                    case R.id.bottom_list:
                        replaceFragment(fragment);
                        break;
                    case R.id.bottom_setting:
                        fm.popBackStack();

                }
                return true;
            }
        });

        fragmentTransaction.add(R.id.main_frame, homeFragment).commit();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("DataBetweenSA"));
    }

    void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getIntExtra("data", 1) == 1) {
                fragment.setList();
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(listDeleteMode){
            fragment.cancelDelete();
        }
        else{
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            }
            else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void isDeleting(boolean b) {
        listDeleteMode = b;
    }
}
