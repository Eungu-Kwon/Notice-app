package com.eungu.notice;

import com.eungu.notice.DBManager.DBData;

import java.util.Calendar;

public class ComputeClass {
    public final String compute_date(DBData data){          //call when today's time is after then data's time
        Calendar now = Calendar.getInstance();
        Calendar temp = data.getTime();
        temp.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
        int day_data = data.getRingData();
        switch (data.getRingCategory()){
            case DBData.RING_ONCE:
                if(temp.compareTo(now) < -1) temp.add(Calendar.DATE, 1);
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
}
