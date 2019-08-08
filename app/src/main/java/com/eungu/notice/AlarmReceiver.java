package com.eungu.notice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.eungu.notice.DBManager.DBData;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        DBData data = (DBData)intent.getSerializableExtra("mydata");
        Log.i("mTag", "aa");
        //Toast.makeText(context, data.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
