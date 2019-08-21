package com.eungu.notice.Extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.eungu.notice.List_Menu.NotiService;
import com.eungu.notice.MainActivity;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.i("mTag", "Received Boot Completed");
            SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
            if(settings.getBoolean("isRunning", false)){
                Log.i("mTag", "Run Service");
                Intent main_service = new Intent(context.getApplicationContext(), NotiService.class);
                if (Build.VERSION.SDK_INT >= 26) {
                    context.startForegroundService(main_service);
                }
                else {
                    context.startService(main_service);
                }
            }
        }
    }
}
