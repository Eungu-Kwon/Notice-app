package com.eungu.notice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eungu.notice.DBManager.AlarmDBHelper;
import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.fragments.DateSetListener;
import com.eungu.notice.fragments.FragmentEmpty;
import com.eungu.notice.fragments.FragmentMonth;
import com.eungu.notice.fragments.FragmentOnce;
import com.eungu.notice.fragments.FragmentWeekOfDay;

import java.util.Calendar;

public class AlarmSettingActivity extends AppCompatActivity implements DateSetListener {

    Calendar time = null;
    String title, content;
    int day = -1, ring_cat, content_cat;
    boolean isNew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_alarm);
        Intent intent = getIntent();
        isNew = intent.getExtras().getBoolean("isNew", true);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.add(R.id.condition_fragment, new FragmentEmpty()).commit();
        setConditionButton();
        setDoneButton();
    }

    void setConditionButton(){
        final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        final Button set_condition_btn = findViewById(R.id.set_condition_btn);
        set_condition_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(set_condition_btn.getWindowToken(), 0);
                day = -1;
                final CharSequence[] oItem = {"시간 선택", "요일별", "월별"};

                FragmentManager fm = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = fm.beginTransaction();

                AlertDialog.Builder oDialog = new AlertDialog.Builder(AlarmSettingActivity.this);
                oDialog.setTitle("조건을 선택하세요")
                        .setItems(oItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        FragmentOnce fragOnce = new FragmentOnce();
                                        fragmentTransaction.replace(R.id.condition_fragment, fragOnce).commit();
                                        set_condition_btn.setText("시간을 선택해주세요");
                                        break;
                                    case 1:
                                        FragmentWeekOfDay fragWeek = new FragmentWeekOfDay();
                                        fragmentTransaction.replace(R.id.condition_fragment, fragWeek).commit();
                                        set_condition_btn.setText("요일을 선택해주세요");
                                        day = 0;
                                        break;
                                    case 2:
                                        FragmentMonth fragmonth = new FragmentMonth();
                                        fragmentTransaction.replace(R.id.condition_fragment, fragmonth).commit();
                                        set_condition_btn.setText("날짜를 선택해주세요");
                                        break;
                                }
                                time = null;
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
                EditText title_edit = findViewById(R.id.title_text);
                EditText content_edit = findViewById(R.id.content_text);
                title = title_edit.getText().toString();
                content = content_edit.getText().toString();
                if(day == 0){
                    Toast.makeText(getApplicationContext(), "요일을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(time == null){
                    Toast.makeText(getApplicationContext(), "시간을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(content.equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlarmDBHelper dbHelper = new AlarmDBHelper(getApplicationContext(), "ALARM_TABLE", null, 1);
                DBData dbData = new DBData(time, ring_cat, content_cat, day, title, content,true);
                dbHelper.addData(dbData);
                finish();
            }
        });
    }

    @Override
    public void setData(Calendar time, int dayOfWeek, int cat_ring, int cat_content){
        this.time = time;
        this.day = dayOfWeek;
        this.ring_cat = cat_ring;
        this.content_cat = cat_content;
    }

    @Override
    public void setBtnText(String str) {
        Button b = (Button)findViewById(R.id.set_condition_btn);
        b.setText(str);
    }
}
