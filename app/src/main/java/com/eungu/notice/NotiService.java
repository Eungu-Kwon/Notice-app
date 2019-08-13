package com.eungu.notice;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.eungu.notice.DBManager.AlarmDBHelper;
import com.eungu.notice.DBManager.DBData;

public class NotiService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "main");

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("서비스 실행중")
                .setContentText("TEST")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setNumber(0);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nCh = new NotificationChannel("main", "메인 채널", NotificationManager.IMPORTANCE_LOW);
            nCh.setShowBadge(false);
            notificationManager.createNotificationChannel(nCh);
        }

        startForeground(1, builder.build());

        setAlarm();

        return super.onStartCommand(intent, flags, startId);
    }

    void setAlarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
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
//            else{
//                Intent mAlarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
//                mAlarmIntent.putExtra("mydata", i);
//
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                alarmManager.cancel(pendingIntent);
//                pendingIntent.cancel();
//            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
