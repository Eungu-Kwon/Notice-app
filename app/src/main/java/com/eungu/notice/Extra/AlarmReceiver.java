package com.eungu.notice.Extra;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.eungu.notice.DBManager.*;
import com.eungu.notice.Extra.ComputeClass;
import com.eungu.notice.R;

public class AlarmReceiver extends BroadcastReceiver {
    AlarmDBHelper dbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        dbHelper = new AlarmDBHelper(context, "ALARM_TABLE", null, 1);
        int data = intent.getIntExtra("mydata", -1);
        if(data!= -1){
            DBData dbdata = dbHelper.getData(data);
            RingRing(context, dbdata.getTitle(), dbdata.getContent(), data);
            dbdata.setNowEnable(false);
            ComputeClass compute = new ComputeClass();
            dbdata.setTimeFromText(compute.compute_date(dbdata));
            dbHelper.updateData(dbdata, data);
            Intent sendData = new Intent("DataBetweenSA");
            intent.putExtra("data", 1);
            LocalBroadcastManager.getInstance(context).sendBroadcast(sendData);
        }
    }

    void RingRing(Context context, String title, String content, int id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND)
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
}
