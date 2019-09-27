package com.eungu.notice.Home_Menu;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.eungu.notice.DBManager.AlarmDBHelper;
import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.Extra.AlarmReceiver;
import com.eungu.notice.Extra.SettingDataHelper;
import com.eungu.notice.R;

public class NotiService extends Service {
    AlarmManager alarmManager = null;
    Intent mainIntent;
    PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SettingDataHelper dataHelper = new SettingDataHelper(getApplicationContext());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "main");
//        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);

        switch(Integer.parseInt(dataHelper.getStringData(SettingDataHelper.MAIN_CATEGORY, "0"))){
            case 1:
                mainIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataHelper.getStringData(SettingDataHelper.URL, null)));
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 945, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case 2:
                mainIntent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(dataHelper.getStringData(SettingDataHelper.APP, null));
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 945, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case 3:
                mainIntent = new Intent("android.intent.action.DIAL", Uri.parse(dataHelper.getStringData(SettingDataHelper.CALL, null)));
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 945, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
        }


        builder.setSmallIcon(R.mipmap.ic_main_icon_foreground)
                .setContentTitle(dataHelper.getStringData(SettingDataHelper.MAIN_TITLE, "알림 제목"))
                .setContentText(dataHelper.getStringData(SettingDataHelper.MAIN_CONTENT, "문구를 변경하려면 터치"))
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentIntent(pendingIntent)
                .setNumber(0);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nCh = new NotificationChannel("main", "메인 채널", NotificationManager.IMPORTANCE_LOW);
            nCh.setShowBadge(false);
            notificationManager.createNotificationChannel(nCh);
        }

        startForeground(678, builder.build());

        SharedPreferences setting = getApplicationContext().getSharedPreferences(SettingDataHelper.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("isRunning", true);
        editor.commit();

        return super.onStartCommand(intent, flags, startId);
    }

    void setAlarm(){
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        AlarmDBHelper dbHelper = new AlarmDBHelper(getApplicationContext(), "ALARM_TABLE", null, 1);

        for(int i = 0; i < dbHelper.getItemsCount(); i++){
            DBData data = dbHelper.getData(i);

            if(data.isNowEnable() == 1) {
                Intent mAlarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
                mAlarmIntent.putExtra("mydata", i);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (Build.VERSION.SDK_INT >= 23)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, data.getTime().getTimeInMillis(), pendingIntent);
                else if (Build.VERSION.SDK_INT >= 19)
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, data.getTime().getTimeInMillis(), pendingIntent);
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP, data.getTime().getTimeInMillis(), pendingIntent);
            }
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        AlarmDBHelper dbHelper = new AlarmDBHelper(getApplicationContext(), "ALARM_TABLE", null, 1);
//        for(int i = 0; i < dbHelper.getItemsCount(); i++) {
//            Intent mAlarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.cancel(pendingIntent);
//            pendingIntent.cancel();
//        }

        SharedPreferences setting = getApplicationContext().getSharedPreferences(SettingDataHelper.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("isRunning", false);
        editor.commit();

    }
}
