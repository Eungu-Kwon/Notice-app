package com.eungu.notice.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

public class AlarmDBHelper extends SQLiteOpenHelper {

    private static final String ALARM_TABLE = "ALARM_TABLE";
    private static final String CONTENT_CATEGORY = "CONTENT_CATEGORY";
    private static final String CATEGORY = "CATEGORY";
    private static final String WHENTORING = "WHENTORING";
    private static final String TITLE = "TITLE";
    private static final String RING_DATA = "RINGDATA";
    private static final String CONTENT = "CONTENT";
    private static final String ENABLE = "ENABLE";

    public AlarmDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ALARM_TABLE(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " + CONTENT_CATEGORY + " INTEGER" + ", " + CATEGORY +  " INTEGER, " +
                WHENTORING + " TEXT, " + RING_DATA + " INTEGER, " + TITLE + " TEXT, " + CONTENT + " TEXT, " + ENABLE + " INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ALARM_TABLE");
        onCreate(db);
    }

    public void addData(DBData item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTENT_CATEGORY, item.getContentCategory());
        values.put(CATEGORY, item.getRingCategory());
        values.put(WHENTORING, item.getTimeToText());
        values.put(RING_DATA, item.getRingData());
        values.put(TITLE, item.getTitle());
        values.put(CONTENT, item.getContent());
        values.put(ENABLE, item.isNowEnable());

        db.insert(ALARM_TABLE, null, values);
        db.close();
    }

    public DBData getData(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ALARM_TABLE, null);
        cursor.moveToFirst();
        cursor.move(id);
        int cate = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CATEGORY)));
        DBData dbData = new DBData(Calendar.getInstance(), cate, Integer.parseInt(cursor.getString(cursor.getColumnIndex(CONTENT_CATEGORY))), cursor.getInt(cursor.getColumnIndex(RING_DATA)),
                cursor.getString(cursor.getColumnIndex(TITLE)), cursor.getString(cursor.getColumnIndex(CONTENT)), true);
        dbData.setTimeFromText(cursor.getString(3));
        if(cursor.getInt(cursor.getColumnIndex(ENABLE)) == 0)
            dbData.setNowEnable(false);

        return dbData;
    }

    public int updateData(DBData item, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONTENT_CATEGORY, item.getContentCategory());
        values.put(CATEGORY, item.getRingCategory());
        values.put(WHENTORING, item.getTimeToText());
        values.put(RING_DATA, item.getRingData());
        values.put(TITLE, item.getTitle());
        values.put(CONTENT, item.getContent());
        values.put(ENABLE, item.isNowEnable());

        int ret = db.update(ALARM_TABLE, values, "_ID="+(id+1), null);
        db.close();
        return ret;
    }

    public boolean deleteColumn(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        int ret = db.delete(ALARM_TABLE, "_id="+(id+1), null);

        db.close();
        return ret > 0;
    }

    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + ALARM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int ret = cursor.getCount();
        cursor.close();

        return ret;
    }
}
