package com.eungu.notice.Extra;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.eungu.notice.DBManager.DBData;
import com.eungu.notice.List_Menu.NotiService;

import java.util.Calendar;

public class ComputeClass {
    public final String compute_date(DBData data){          //call when today's time is after then data's time
        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        Calendar temp = data.getTime();
        temp.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
        int day_data = data.getRingData();
        switch (data.getRingCategory()){
            case DBData.RING_ONCE:
                if(temp.compareTo(now) == -1) temp.add(Calendar.DATE, 1);
                data.setTime(temp);
                break;
            case DBData.RING_DAYOFWEEK:
                for(int i = 1; i <= 7; i++){
                    temp.add(Calendar.DATE, 1);
                    if(((1 << (temp.get(Calendar.DAY_OF_WEEK) - 1)) & day_data) > 0){
                        data.setTime(temp);
                        break;
                    }
                }
                break;
            case DBData.RING_MONTH:
                for(int i = 1; i <= 31; i++){
                    temp.add(Calendar.DATE, 1);
                    if(((1 << (temp.get(Calendar.DAY_OF_MONTH) - 1)) & day_data) > 0){
                        data.setTime(temp);
                        break;
                    }
                }
                break;
        }
        return data.getTimeToText();
    }

    public static final Boolean isLaunchingService(Context mContext){

//        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (c.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//
//        return  false;

        SharedPreferences setting = mContext.getSharedPreferences(SettingDataHelper.PREFS_NAME, Context.MODE_PRIVATE);
        boolean b = setting.getBoolean("isRunning", false);
        return b;
    }
}
