package com.eungu.notice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class NotiService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("서비스 실행중")
                .setContentText("TEST")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setNumber(0);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nCh = new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_LOW);
            nCh.setShowBadge(false);
            notificationManager.createNotificationChannel(nCh);
        }

        startForeground(1, builder.build());
        return super.onStartCommand(intent, flags, startId);
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
