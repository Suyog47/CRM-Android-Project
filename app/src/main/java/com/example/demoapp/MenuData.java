package com.example.demoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demoapp.BackgroundProcessClass.BackupAndRestoreDBs;
import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.DailyActivityClasses.DailyActivity;
import com.example.demoapp.DailyActivityClasses.UpdateDailyActivity;
import com.example.demoapp.DatesClasses.Impdates;
import com.example.demoapp.EventClasses.InsertLog;
import com.example.demoapp.EventClasses.ShowLog;
import com.example.demoapp.EventClasses.SweetMemories;
import com.example.demoapp.EventClasses.UpdateLog;
import com.example.demoapp.NotesClasses.Notes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuData extends Activity {

    ImageView menuimg;
    CommonFunctions cf = new CommonFunctions();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_data);
        menuimg = findViewById(R.id.menuImg);

        //Setting up new Thread to set Bitmap Scaled Images
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int img = R.drawable.menu;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = cf.getScaledImage(getApplicationContext(), img, display);
                menuimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        List<Callable<Void>> tasklist = new ArrayList<>();
        tasklist.add(call1);

        ExecutorService executor = Executors.newCachedThreadPool();
        try{executor.invokeAll(tasklist);}
        catch(Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}
        finally {
            executor.shutdown();
        }
    }

    //Function to start Insert Log Activity
    public void insertLog(View v){
        Intent al = new Intent(this, InsertLog.class);
        startActivity(al);
    }

    //Function to start Show Log Activity
    public void showLog(View v) {
        Intent a2 = new Intent(this, ShowLog.class);
        startActivity(a2);
    }

    //Function to start Daily-Activities Activity
    public void dailyActivity(View v){
        Intent a3 = new Intent(this, DailyActivity.class);
        startActivity(a3);
    }

    //Function to start Sweet-Memory Activity
    public void sweetMemories(View v){
        Intent a4 = new Intent(this, SweetMemories.class);
        startActivity(a4);
    }

    //Function to start Update Log Activity
    public void updateLog(View v){
        Intent a5 = new Intent(this, UpdateLog.class);
        startActivity(a5);
    }

    //Function to start Update daily-activities Activity
    public void updateDailyActivity(View v){
        Intent a6 = new Intent(this, UpdateDailyActivity.class);
        startActivity(a6);
    }

    //Function to start Notes Activity
    public void notes(View v){
        Intent a7 = new Intent(this, Notes.class);
        startActivity(a7);
    }

    //Function to start ImpDates Activity
    public void impdates(View v){
        Intent a8 = new Intent(this, Impdates.class);
        startActivity(a8);
    }

    //Function to start BackUp and Restore Activity
    public void dbcontrols(View v){
        Intent a9 = new Intent(this, BackupAndRestoreDBs.class);
        startActivity(a9);
    }

}
