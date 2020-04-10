package com.example.demoapp.SqlliteDBClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventSqlliteDbService extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public final static String DATABASE_NAME = "Memory.db";
    public final static String TABLE_NAME = "Event_table";
    public final static String COL_1 = "Year";
    public final static String COL_2 = "Date";
    public final static String COL_3 = "Subject";
    public final static String COL_4 = "Event";
    public final static String COL_5 = "Favourite";

    public EventSqlliteDbService(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL("create table " + TABLE_NAME + "(Year Integer, Date Text, Subject Text, Event Text, Favourite Boolean)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    //Function for Inserting data
    public String insertData(int Year, String Date, String Subject, String Event, boolean Favourite){
       db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, Year);
        values.put(COL_2, Date);
        values.put(COL_3, Subject);
        values.put(COL_4, Event);
        values.put(COL_5, Favourite);
        long result = db.insert(TABLE_NAME,null, values);
        if(result == -1) {
          return "Something Went Wrong Bro!";
        }
        else
            return "Data Inserted!";
        }


    //Function for Getting all Years
    public Cursor getYear(){
        db = this.getReadableDatabase();
        Cursor years = db.rawQuery("select distinct(Year) from Event_table Order By Year asc",null);
        return years;
       }

    //Function for Getting Dates of selected Year
       public Cursor getDates(int Year){
          db = this.getReadableDatabase();
          Cursor dates = db.rawQuery("select * from Event_table where Year = " + Year + " Order By date asc",null);
          return dates;
        }


    //Function for Getting sweet-mem Years
    public Cursor getSweetYears(){
        db = this.getReadableDatabase();
        Cursor dates = db.rawQuery("select distinct(Year) from Event_table where Favourite = 1 Order By Year asc",null);
        return dates;
    }


    //Function for Getting Dates of selected sweet-mem Year
    public Cursor getSweetDates(int Year){
        db = this.getReadableDatabase();
        Cursor dates = db.rawQuery("select * from Event_table where Favourite = 1 and Year = '" + Year + "' Order By Date asc",null);
        return dates;
    }


    //Function for Getting Events
    public Cursor getEvent(String Date){
        db = this.getReadableDatabase();
        Cursor Data = db.rawQuery("select * from Event_table where Date = '"+Date+"'" ,null);
        return Data;
    }


    //Function for Updating the Event
    public boolean updateEvent(String Date, String Subject, String Event, Boolean Fav){
      db = this.getWritableDatabase();
      ContentValues values = new ContentValues();
        values.put(COL_3, Subject);
        values.put(COL_4, Event);
        values.put(COL_5, Fav);
        db.update(TABLE_NAME,values,"Date = ?", new String[] {Date});
        return true;
    }


    //Function for Deleting the Event
    public Integer deleteEvent(String Date){
        db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"Date = ?",new String[] {Date});
    }
}
