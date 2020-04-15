package com.example.demoapp.SqlliteDBClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DActivitySqlliteDbService extends SQLiteOpenHelper {

    SQLiteDatabase db;
    public final static String DATABASE_NAME = "DActivity.db";
    public final static String TABLE_NAME = "Activity_table";
    public final static String COL_1 = "Day";
    public final static String COL_2 = "Activity";
    public final static String COL_3 = "Timer";
    public final static String COL_4 = "TimerStatus";

    public DActivitySqlliteDbService(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +TABLE_NAME+ "(Day Text, Activity Text, Timer Text, TimerStatus int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //Function for Inserting data
    public String insertActivity(String Day, String Activity){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, Day);
        values.put(COL_2, Activity);
        long result = db.insert(TABLE_NAME,null,values);
        if(result == -1){
            return "Not Inserted!";
        }
        else{return "Activity Inserted!"; }
    }


    //Function for Update-Activity data
    public boolean updateActivity(String Day, String Activity){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, Activity);
        db.update(TABLE_NAME, values,"Day = ?", new String[] {Day});
        return true;
        }


    //Function for Update-Timer data
    public boolean updateTimer(String Day, String Timer){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3, Timer);
        db.update(TABLE_NAME, values,"Day = ?", new String[] {Day});
        return true;
    }


    //Function for Getting all data
    public Cursor getActivities(String Day){
        db = this.getReadableDatabase();
        Cursor data = db.rawQuery("Select * from Activity_table where Day = '"+Day+"'",null);
        return data;
    }


    //Function for Getting all timer data
    public Cursor getTimer(String Day){
        db = this.getReadableDatabase();
        Cursor data = db.rawQuery("Select Timer, TimerStatus from Activity_table where Day = '"+Day+"'",null);
        return data;
    }


    //Function for Updating the timer status
    public boolean setTimerStatus(String Day, int timerStatus){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_4, timerStatus);
        db.update(TABLE_NAME, values,"Day = ?", new String[] {Day});
        return true;
    }
}
