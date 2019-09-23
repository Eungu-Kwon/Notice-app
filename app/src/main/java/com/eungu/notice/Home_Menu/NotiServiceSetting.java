package com.eungu.notice.Home_Menu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.eungu.notice.Extra.SettingDataHelper;
import com.eungu.notice.List_Menu.NotiService;
import com.eungu.notice.R;

public class NotiServiceSetting extends AppCompatActivity implements RadioButton.OnCheckedChangeListener, Button.OnClickListener {
    int category = 0;

    EditText urlText;
    Button appButton;
    Button addressButton;
    String appString;
    String addressString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti_service_setting);
        Button done_btn = findViewById(R.id.noti_done);
        final EditText title = findViewById(R.id.noti_title_edit);
        final EditText content = findViewById(R.id.noti_content_edit);

        final SettingDataHelper dataHelper = new SettingDataHelper(getApplicationContext());

        title.setText(dataHelper.getStringData(SettingDataHelper.MAIN_TITLE, ""));
        content.setText(dataHelper.getStringData(SettingDataHelper.MAIN_CONTENT, ""));
        category = Integer.parseInt(dataHelper.getStringData(SettingDataHelper.MAIN_CATEGORY, "0"));
        setRadioButton(dataHelper);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (category){
                    case 1:
                        String mUrl = urlText.getText().toString();
                        if(mUrl.equals("")){
                            Toast.makeText(getApplicationContext(), "주소를 설정해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!mUrl.startsWith("http://")){
                            mUrl = "http://" + mUrl;
                        }
                        dataHelper.settingStringData(SettingDataHelper.URL, mUrl);
                        break;
                    case 2:
                        dataHelper.settingStringData(SettingDataHelper.APP, appString);
                        break;
                    case 3:
                        if(addressString == null || addressString == ""){
                            Toast.makeText(getApplicationContext(), "연락처를 설정해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dataHelper.settingStringData(SettingDataHelper.CALL, addressString);
                        break;
                }

                dataHelper.settingStringData(SettingDataHelper.MAIN_TITLE, title.getText().toString());
                dataHelper.settingStringData(SettingDataHelper.MAIN_CONTENT, content.getText().toString());
                dataHelper.settingStringData(SettingDataHelper.MAIN_CATEGORY, category+"");

                Intent service = new Intent(v.getContext(), NotiService.class);
                stopService(service);
                if(Build.VERSION.SDK_INT >= 26)
                    v.getContext().startForegroundService(service);
                else
                    v.getContext().startService(service);

                finish();
            }
        });
        setButtons();
    }

    void setRadioButton(SettingDataHelper dataHelper){
        RadioButton rb0 = findViewById(R.id.noti_radio_none);
        RadioButton rb1 = findViewById(R.id.noti_radio_url);
        RadioButton rb2 = findViewById(R.id.noti_radio_app);
        RadioButton rb3 = findViewById(R.id.noti_radio_call);
        urlText = findViewById(R.id.noti_text_url);
        appButton = findViewById(R.id.noti_button_app);
        addressButton = findViewById(R.id.noti_button_address);
        rb0.setOnCheckedChangeListener(this);
        rb1.setOnCheckedChangeListener(this);
        rb2.setOnCheckedChangeListener(this);
        rb3.setOnCheckedChangeListener(this);

        switch (category){
            case 0:
                rb0.setChecked(true);
                break;
            case 1:
                rb1.setChecked(true);
                urlText.setText(dataHelper.getStringData(SettingDataHelper.URL, ""));
                break;
            case 2:
                rb2.setChecked(true);
                break;
            case 3:
                rb3.setChecked(true);
                addressString = dataHelper.getStringData(SettingDataHelper.CALL, "");
                break;
        }
    }

    void setButtons(){
        addressButton.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            if(buttonView.getId() == R.id.noti_radio_none){
                category = 0;
            }
            if(buttonView.getId() == R.id.noti_radio_url){
                category = 1;
                urlText.setVisibility(View.VISIBLE);
            }
            else if(buttonView.getId() == R.id.noti_radio_app){
                category = 2;
                appButton.setVisibility(View.VISIBLE);
            }
            else if(buttonView.getId() == R.id.noti_radio_call){
                category = 3;
                addressButton.setVisibility(View.VISIBLE);
            }
        }
        else{
            if(buttonView.getId() == R.id.noti_radio_url){
                urlText.setVisibility(View.GONE);
            }
            else if(buttonView.getId() == R.id.noti_radio_app){
                appButton.setVisibility(View.GONE);
            }
            else if(buttonView.getId() == R.id.noti_radio_call){
                addressButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.noti_button_address:
                Intent addressIntent = new Intent(Intent.ACTION_PICK);
                addressIntent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(addressIntent, 0);
                break;
            case R.id.noti_button_app:
                PackageManager packageManager = getPackageManager();

                Intent appIntent = new Intent(Intent.ACTION_MAIN);
                appIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                startActivityForResult(appIntent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            Cursor cursor = getContentResolver().query(data.getData(), new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            addressString = cursor.getString(1);
            addressString = addressString.replace("-", "");
            addressString = "tel:" + addressString;
            addressButton.setText(cursor.getString(0) + "님께 전화");
        }
    }
}
