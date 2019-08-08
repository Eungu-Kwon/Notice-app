package com.eungu.notice.DBManager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DBData implements Serializable {
    public static final int RING_ONCE = 97;
    public static final int RING_DAYOFWEEK = 98;
    public static final int RING_MONTH = 99;


    public static final int CONTENT_NORMAL = 127;

    private final static String DATE_PAT = "yyyy-MM-dd HH:mm:ss";

    Calendar time;
    int ringCategory, ringData;
    int contentCategory;
    String title, content;
    boolean  nowEnable;
    public DBData(Calendar _time, int _ringCategory, int _contentCategory, int ringData, String title, String _content, boolean _nowEnable) {
        this.time = _time;
        this.ringCategory = _ringCategory;
        this.contentCategory = _contentCategory;
        this.ringData = ringData;
        this.title = title;
        this.content = _content;
        this.nowEnable = _nowEnable;
    }

    public void updateData(Calendar _time, int _ringCategory, int _contentCategory, int ringData, String title, String _content, boolean _nowEnable) {
        this.time = _time;
        this.ringCategory = _ringCategory;
        this.contentCategory = _contentCategory;
        this.ringData = ringData;
        this.title = title;
        this.content = _content;
        this.nowEnable = _nowEnable;
    }

    public String getTimeToText(){
        SimpleDateFormat format = new SimpleDateFormat(DATE_PAT);
        String ret = format.format(time.getTime());
        return ret;
    }

    public int setTimeFromText(String str){
        SimpleDateFormat format = new SimpleDateFormat(DATE_PAT);
        Date dateTime = null;
        try {
            dateTime = format.parse(str);
        } catch (ParseException e) {
            return -1;
        }
        this.time.setTime(dateTime);
        return 0;
    }

    public int getRingData() {
        return ringData;
    }

    public void setRingData(int ringData) {
        this.ringData = ringData;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getRingCategory() {
        return ringCategory;
    }

    public void setRingCategory(int ringCategory) {
        this.ringCategory = ringCategory;
    }

    public int getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(int contentCategory) {
        this.contentCategory = contentCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int isNowEnable() {
        if(nowEnable)
            return 1;
        else return 0;
    }

    public void setNowEnable(boolean nowEnable) {
        this.nowEnable = nowEnable;
    }
}
