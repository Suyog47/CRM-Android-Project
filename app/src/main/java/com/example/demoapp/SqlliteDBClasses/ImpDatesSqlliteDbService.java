package com.example.demoapp.SqlliteDBClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImpDatesSqlliteDbService extends SQLiteOpenHelper {

    SQLiteDatabase db;
    public final static String DATABASE_NAME = "Dates.db";
    public final static String TABLE_NAME = "Impdates_table";
    public final static String COL_1 = "Date";
    public final static String COL_2 = "Event";

    public ImpDatesSqlliteDbService(Context context){
    super(context, DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(Date Text, Event Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }


    //Function for Inserting date
    public String insertDate(String date, String event){
        db = this.getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(COL_1, date);
        val.put(COL_2, event);
        long res = db.insert(TABLE_NAME,null,val);
        if(res == -1){
            return "Something Went Wrong!";
        }
        else
            return "Date Inserted";
    }


    //Function for Getting all dates
    public Cursor getDates(){
        db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Impdates_table",null);
        return res;
    }


    //Function for Getting Event for selected date
    public Cursor getCertainDate(String date){
        db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Impdates_table where Date = '" + date +"'",null);
        return res;
    }


    //Function for Deleting the date
    public Integer deleteDate(String date){
        db = this.getReadableDatabase();
        return db.delete(TABLE_NAME,"Date = ?",new String[] {date});
    }
}
