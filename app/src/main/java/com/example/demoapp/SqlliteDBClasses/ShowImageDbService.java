package com.example.demoapp.SqlliteDBClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShowImageDbService extends SQLiteOpenHelper {

    SQLiteDatabase db;
    private final static String DATABASE_NAME = "Image.db";
    private final static String TABLE_NAME = "PImages_table";
    private final static String COL_1 = "Image";
    private final static String COL_2 = "Date";
    private final static String COL_3 = "Description";

    public ShowImageDbService(Context context){
      super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL("Create table " + TABLE_NAME + " (Image Blob, Date Text, Description Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public String insertImages(byte[] img, String desc, String date){
        db = getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(COL_1, img);
        val.put(COL_2, date);
        val.put(COL_3, desc);
        long result = db.insert(TABLE_NAME,null, val);
        if(result != -1){
            return "Success";
        }
        else
            return "Failed";
    }


    public Cursor getImages(int l){
        db  = getReadableDatabase();
        Cursor res = db.rawQuery("select * from PImages_table Limit '"+ l +"', 6",null);
        return res;
    }

    public Cursor getSelectedImage(String desc){
        db  = getReadableDatabase();
        Cursor res = db.rawQuery("select Image from PImages_table where Description = '"+ desc +"'",null);
        return res;
    }

    public Integer delPhoto(String desc){
        db = getWritableDatabase();
        return db.delete(TABLE_NAME,"Description = ?", new String[] {desc});
    }

}
