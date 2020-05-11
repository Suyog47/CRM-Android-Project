package com.example.demoapp.SqlliteDBClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.demoapp.NotesClasses.Notes;

public class NotesSqlliteDbService extends SQLiteOpenHelper {

    SQLiteDatabase db;
    public final static String DATABASE_NAME = "Notes.db";
    public final static String TABLE_NAME = "Notes_table";
    public final static String COL_1 = "Notes";
    public final static String COL_2 = "Flag";

    public NotesSqlliteDbService(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create table "+ TABLE_NAME +"(Notes Text, Flag Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }


    //Function for Inserting a Note
    public String insertNote(String Note, String Flag){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, Note);
        values.put(COL_2, Flag);
        long res = db.insert(TABLE_NAME,null,values);
        if(res == -1){
            return "Something Went Wrong!";
        }
        else
        return "Notes Inserted";
    }

    public boolean updateNote(String prevNote, String Notes, String Flag){
        db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, Notes);
        values.put(COL_2, Flag);
        db.update(TABLE_NAME, values,"Notes = ?", new String[] {prevNote});
        return true;
    }


    //Function for Getting all Notes
    public Cursor getNotes(){
        db = this.getReadableDatabase();
        Cursor nt = db.rawQuery("select * from "+ TABLE_NAME,null);
        return nt;
    }


    //Function for Deleting the Note
    public Integer deleteNote(String note){
        db = this.getReadableDatabase();
        return db.delete(TABLE_NAME,"Notes = ?", new String[] {note});
    }

}
