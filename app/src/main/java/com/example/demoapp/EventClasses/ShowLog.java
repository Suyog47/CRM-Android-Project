package com.example.demoapp.EventClasses;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.SqlliteDBClasses.EventSqlliteDbService;
import com.example.demoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowLog extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner year, date;
    TextView event, subject;
    EventSqlliteDbService db;
    ImageView showimg, fav;
    CommonFunctions cf = new CommonFunctions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlog);

        date = findViewById(R.id.dateDropDown);
        year = findViewById(R.id.yearDropDown);
        subject = findViewById(R.id.subjectLabel);
        subject.setMovementMethod(new ScrollingMovementMethod());
        event = findViewById(R.id.eventLabel);
        event.setMovementMethod(new ScrollingMovementMethod());
        fav = findViewById(R.id.fav);
        year.setOnItemSelectedListener(this);
        showimg = findViewById(R.id.showImg);

        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting up Bitmap Scaled Images
                int img = R.drawable.showevents;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = cf.getScaledImage(getApplicationContext(), img, display);
                showimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Get all Event Years registered in Db
                db = new EventSqlliteDbService(getApplicationContext());
                Cursor res = db.getYear();
                year.setAdapter(cf.setYears(getApplicationContext(), res));
                return null;
            }
        };

        List<Callable<Void>> taskList = new ArrayList<>();
        taskList.add(call1);
        taskList.add(call2);

        ExecutorService executor = Executors.newCachedThreadPool();
        try { executor.invokeAll(taskList); }
        catch (InterruptedException ie) { Toast.makeText(this, "Something wrong in Threads", Toast.LENGTH_SHORT).show(); }
        finally{ executor.shutdown(); }
    }


    //Function to get dates from selected Year
   public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        String yr = year.getSelectedItem().toString();
       db = new EventSqlliteDbService(this);
       date.setAdapter(cf.setDates(this,db,yr,"normal"));
   }

    public void onNothingSelected(AdapterView<?> parent) { }


    //Function to get Event for Selected Date
    public void watchEvent(View v) {
        int fv = 0;
        db = new EventSqlliteDbService(this);
        Cursor res = db.getEvent(date.getSelectedItem().toString());
        while(res.moveToNext()){
            event.setText(res.getString(3));
            subject.setText(res.getString(2));
            fv = res.getInt(4);
        }
        if(fv == 1){ fav.setColorFilter(Color.RED);}
        else{ fav.setColorFilter(Color.BLACK);}
      }

}
