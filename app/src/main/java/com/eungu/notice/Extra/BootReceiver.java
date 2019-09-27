package com.eungu.notice.Extra;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.eungu.notice.DBManager.AlarmDBHelper;
import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.Home_Menu.NotiService;
import com.eungu.notice.MainActivity;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            if(settings.getBoolean("isRunning", false)){
                Intent main_service = new Intent(context.getApplicationContext(), NotiService.class);
                if (Build.VERSION.SDK_INT >= 26) {
                    context.startForegroundService(main_service);
                }
                else {
                    context.startService(main_service);
                }
            }
            setAlarm(context);
        }
    }
    void setAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        AlarmDBHelper dbHelper = new AlarmDBHelper(context, "ALARM_TABLE", null, 1);

        for(int i = 0; i < dbHelper.getItemsCount(); i++){
            DBData data = dbHelper.getData(i);

            if(data.isNowEnable() == 1) {
                Intent mAlarmIntent = new Intent(context, AlarmReceiver.class);
                mAlarmIntent.putExtra("mydata", i);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (Build.VERSION.SDK_INT >= 23)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, data.getTime().getTimeInMillis(), pendingIntent);
                else if (Build.VERSION.SDK_INT >= 19)
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, data.getTime().getTimeInMillis(), pendingIntent);
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP, data.getTime().getTimeInMillis(), pendingIntent);
            }
        }
    }
}
