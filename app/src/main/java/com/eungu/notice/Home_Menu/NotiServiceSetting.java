package com.eungu.notice.Home_Menu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.eungu.notice.Extra.ComputeClass;
import com.eungu.notice.Extra.SettingDataHelper;
import com.eungu.notice.R;

public class NotiServiceSetting extends AppCompatActivity implements RadioButton.OnCheckedChangeListener, Button.OnClickListener {
    int category = 0;
    String pName;
    String appName;
    EditText urlText;
    Button appButton;
    Button addressButton;
    String addressString;
    String appPackage;

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
                dataHelper.settingStringData(SettingDataHelper.PNAME, null);
                dataHelper.settingStringData(SettingDataHelper.APPNAME, null);
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
                        if(appPackage == null || appPackage == ""){
                            Toast.makeText(getApplicationContext(), "어플을 선택해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dataHelper.settingStringData(SettingDataHelper.APPNAME, appName);
                        dataHelper.settingStringData(SettingDataHelper.APP, appPackage);
                        break;
                    case 3:
                        if(addressString == null || addressString == ""){
                            Toast.makeText(getApplicationContext(), "연락처를 설정해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dataHelper.settingStringData(SettingDataHelper.CALL, addressString);
                        dataHelper.settingStringData(SettingDataHelper.PNAME, pName);
                        break;
                }

                dataHelper.settingStringData(SettingDataHelper.MAIN_TITLE, title.getText().toString());
                dataHelper.settingStringData(SettingDataHelper.MAIN_CONTENT, content.getText().toString());
                dataHelper.settingStringData(SettingDataHelper.MAIN_CATEGORY, category+"");

                if(ComputeClass.isLaunchingService(getApplicationContext())){
                    Intent service = new Intent(v.getContext(), NotiService.class);
                    stopService(service);
                    if(Build.VERSION.SDK_INT >= 26)
                        v.getContext().startForegroundService(service);
                    else
                        v.getContext().startService(service);
                }

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
                appPackage = dataHelper.getStringData(SettingDataHelper.APP, null);
                break;
            case 3:
                rb3.setChecked(true);
                addressString = dataHelper.getStringData(SettingDataHelper.CALL, "");
                break;
        }

        pName = dataHelper.getStringData(SettingDataHelper.PNAME, null);
        if(pName != null){
            addressButton.setText(pName + "님께 전화");
        }
        appName = dataHelper.getStringData(SettingDataHelper.APPNAME, null);
        if(appName != null) appButton.setText(appName + " 실행");
    }

    void setButtons(){
        addressButton.setOnClickListener(this);
        appButton.setOnClickListener(this);
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
                Intent appIntent = new Intent(getApplicationContext(), AppSelectActivity.class);
                startActivityForResult(appIntent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 0:
                    Cursor cursor = getContentResolver().query(data.getData(), new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                    cursor.moveToFirst();
                    addressString = cursor.getString(1);
                    addressString = addressString.replace("-", "");
                    addressString = "tel:" + addressString;
                    pName = cursor.getString(0);
                    addressButton.setText(pName + "님께 전화");
                    break;
                case 1:
                    appName = data.getStringExtra("appName");
                    appName = appName.replace("\n", " ");
                    appPackage = data.getStringExtra("appPackage");
                    appButton.setText(appName + " 실행");
                    break;
            }
        }
    }
}
