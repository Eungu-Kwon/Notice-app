package com.eungu.notice;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eungu.notice.DBManager.AlarmDBHelper;
import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.datapicker.AlarmTimePicker;
import com.eungu.notice.fragments.DateSetListener;
import com.eungu.notice.fragments.FragmentOnce;

import java.util.Calendar;

public class AlarmSettingActivity extends AppCompatActivity implements DateSetListener {

    Calendar time;
    String title, content;
    int dayOfWeek, ring_cat, content_cat;
    boolean isNew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_alarm);
        Intent intent = getIntent();
        isNew = intent.getExtras().getBoolean("isNew", true);

        setConditionButton();
        setDoneButton();
    }

    void setConditionButton(){
        Button set_condition_btn = findViewById(R.id.set_condition_btn);
        set_condition_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] oItem = {"시간 선택", "요일별", "월별"};

                AlertDialog.Builder oDialog = new AlertDialog.Builder(AlarmSettingActivity.this);
                oDialog.setTitle("조건을 선택하세요")
                        .setItems(oItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        FragmentOnce frag = new FragmentOnce();
                                        Bundle b = new Bundle();
                                        FragmentManager fm = getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                        fragmentTransaction.add(R.id.condition_fragment, new FragmentOnce());
                                        fragmentTransaction.commit();
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = oDialog.create();
                alertDialog.show();
            }
        });
    }

    void setDoneButton(){
        Button done_btn = findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmDBHelper dbHelper = new AlarmDBHelper(getApplicationContext(), "ALARM_TABLE", null, 1);
                DBData dbData = new DBData(time, ring_cat, content_cat, title, content, true);
                dbHelper.addData(dbData);
                finish();
            }
        });
    }

    @Override
    public void setData(Calendar time, int dayOfWeek, int cat_ring, int cat_content){
        this.time = time;
        this.dayOfWeek = dayOfWeek;
        this.ring_cat = cat_ring;
        this.content_cat = cat_content;
    }
}
