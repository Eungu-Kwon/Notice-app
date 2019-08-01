package com.eungu.notice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDBHelper extends SQLiteOpenHelper {
    public AlarmDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ALARM_TABLE(_ID INTEGER PRIMARY KEY AUTOINCREMENT, CONTENT_CATEGORY INTEGER, CATEGORY INTEGER, " +
                "WHENTORING TEXT, TITLE TEXT, CONTENT TEXT, ENABLE INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
