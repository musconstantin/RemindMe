package com.example.marius.remindme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by marius on 18.12.2016.
 */
/*
|---------------------------------EVENTS TABLE STRUCTURE-----------------------------------|                                                     |
|------------------------------------------------------------------------------------------|
|  COLUMN NAME   |  COLUMN TYPE   |       COLUMN COMMENTS                                  |
|------------------------------------------------------------------------------------------|
|  ID            |     INTEGER    |    Unique identifier(autoincremented)                  |
|  TITLE         |     TEXT       |    Title of the event                                  |
|  DESCRIPTION   |     TEXT       |    Description of the event                            |
|  FREQUENCY     |     TEXT       |    How often the app should send notifications         |
|  FREQTYPE      |     TEXT       |    Frequency type (minutes/hours/days)                 |
|  ACTIVE        |     TEXT       |    1 - if the event is active, 0 otherwise             |
|  CRDATE        |     TEXT       |    The date that the event was created at              |
|  NEXTALERT     |     TEXT       |    When the next notification should be sent           |
|__________________________________________________________________________________________|
 */
public class DBHelper extends SQLiteOpenHelper{
    public static final String c_DATABASE_NAME = "RemindMe.db";
    public static final String c_EVENTS_TABLE_NAME = "events";
    public static final String c_EVENTS_COLUMN_ID = "id";
    public static final String c_EVENTS_COLUMN_TITLE = "title";
    public static final String c_EVENTS_COLUMN_DESCRIPTION = "description";
    public static final String c_EVENTS_COLUMN_FREQUENCY = "frequency";
    public static final String c_EVENTS_COLUMN_FREQUENCYTYPE = "freqtype"; //days/minutes/hours
    public static final String c_EVENTS_COLUMN_ACTIVE = "active"; // 1 - yes; 0 - no
    public static final String c_EVENTS_COLUMN_CREATEDATE = "crdate";
    public static final String c_EVENTS_COLUMN_NEXTALERTTIME = "nextalert"; //the time when the next notification will be sent


    public DBHelper(Context context) {
        super(context, c_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + c_EVENTS_TABLE_NAME +
        "( " + c_EVENTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
        c_EVENTS_COLUMN_TITLE + " TEXT, " +
        c_EVENTS_COLUMN_DESCRIPTION + " TEXT, " +
        c_EVENTS_COLUMN_FREQUENCY + " TEXT, " +
        c_EVENTS_COLUMN_FREQUENCYTYPE + " TEXT, " +
        c_EVENTS_COLUMN_ACTIVE + " TEXT, " +
        c_EVENTS_COLUMN_CREATEDATE + " TEXT, " +
        c_EVENTS_COLUMN_NEXTALERTTIME + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + c_EVENTS_TABLE_NAME);
        onCreate(db);
    }

    public Cursor getList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results = db.rawQuery("select * from " + c_EVENTS_TABLE_NAME, null);
        return results;
    }

    private Cursor getActiveList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results = db.rawQuery("select * from " + c_EVENTS_TABLE_NAME + " where active ='1'", null);
        return results;
    }

    public Cursor getRow(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from " + c_EVENTS_TABLE_NAME + " where id=" + id+"", null);
        return result;
    }

    public int rowCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        int rowCount = (int) DatabaseUtils.queryNumEntries(db, c_EVENTS_TABLE_NAME);
        return rowCount;
    }

    public boolean insertEvent(String title,
                                 String description,
                                 String frequency,
                                 String frequencyType,
                                 String active,
                                 String createDate,
                                 String nextAlertTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c_EVENTS_COLUMN_TITLE, title);
        contentValues.put(c_EVENTS_COLUMN_DESCRIPTION, description);
        contentValues.put(c_EVENTS_COLUMN_FREQUENCY, frequency);
        contentValues.put(c_EVENTS_COLUMN_FREQUENCYTYPE, frequencyType);
        contentValues.put(c_EVENTS_COLUMN_ACTIVE, active);
        contentValues.put(c_EVENTS_COLUMN_CREATEDATE, createDate);
        contentValues.put(c_EVENTS_COLUMN_NEXTALERTTIME, nextAlertTime);
        db.insert(c_EVENTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateEvent(Integer id,
                               String title,
                               String description,
                               String frequency,
                               String frequencyType,
                               String active,
                               String createDate,
                               String nextAlertTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c_EVENTS_COLUMN_TITLE, title);
        contentValues.put(c_EVENTS_COLUMN_DESCRIPTION, description);
        contentValues.put(c_EVENTS_COLUMN_FREQUENCY, frequency);
        contentValues.put(c_EVENTS_COLUMN_FREQUENCYTYPE, frequencyType);
        contentValues.put(c_EVENTS_COLUMN_ACTIVE, active);
        contentValues.put(c_EVENTS_COLUMN_CREATEDATE, createDate);
        contentValues.put(c_EVENTS_COLUMN_NEXTALERTTIME, nextAlertTime);
        db.update(c_EVENTS_TABLE_NAME, contentValues, "id = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public boolean setActiveEvent(Integer id, String active){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(c_EVENTS_COLUMN_ACTIVE, active);
        db.update(c_EVENTS_TABLE_NAME, contentValues, "id = ?", new String[] {Integer.toString(id)});
        return true;
    }
    public Integer deleteEvent(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(c_EVENTS_TABLE_NAME,
                "id = ?",
                new String[] {Integer.toString(id)});
    }

    public ArrayList<Event> getEventsForInterface(){
        ArrayList<Event> eventList = new ArrayList<Event>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = this.getActiveList();
        res.moveToFirst();

        while(!res.isAfterLast()){
            Event temp = new Event();
            temp.setId(res.getInt(res.getColumnIndex(c_EVENTS_COLUMN_ID)));
            temp.setTitle(res.getString(res.getColumnIndex(c_EVENTS_COLUMN_TITLE)));
            temp.setDescription(res.getString(res.getColumnIndex(c_EVENTS_COLUMN_DESCRIPTION)));
            temp.setActive(res.getString(res.getColumnIndex(c_EVENTS_COLUMN_ACTIVE)));
            eventList.add(temp);
            res.moveToNext();
        }

        return eventList;
    }
}
