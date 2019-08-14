package com.eungu.notice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.eungu.notice.DBManager.AlarmDBHelper;
import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.list_maker.AlarmListAdapter;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    AlarmDBHelper dbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper = new AlarmDBHelper(context, "ALARM_TABLE", null, 1);
        int data = intent.getIntExtra("mydata", -1);
        if(data!= -1){
            DBData dbdata = dbHelper.getData(data);
            RingRing(context, dbdata.getTitle(), dbdata.getTimeToText(), data);
            dbdata.setNowEnable(false);
            ComputeClass compute = new ComputeClass();
            dbdata.setTimeFromText(compute.compute_date(dbdata));
            dbHelper.updateData(dbdata, data);
        }
    }

    void RingRing(Context context, String title, String content, int id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title + " id : " + id)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setNumber(0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nCh = new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_HIGH);
            nCh.setShowBadge(false);
            notificationManager.createNotificationChannel(nCh);
        }

        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "PushTest:tag");

        wl.acquire();
        wl.release();
        notificationManager.notify(id, builder.build());
    }

    void compute_date(Context context, DBData dbdata, int id){
        switch (dbdata.getRingCategory()){
            case DBData.RING_ONCE:
                Calendar time = dbdata.getTime();
                time.add(Calendar.DATE, 1);
                dbdata.setNowEnable(false);
                dbHelper.updateData(dbdata, id);
                break;
        }
    }
}
