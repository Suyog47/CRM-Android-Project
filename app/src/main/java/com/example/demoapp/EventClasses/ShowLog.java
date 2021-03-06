package com.example.demoapp.EventClasses;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.CommonFunctionsClass.EventsCF;
import com.example.demoapp.SqlliteDBClasses.EventSqlliteDbService;
import com.example.demoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowLog extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner year, date;
    TextView event, subject, tv;
    ImageView showimg, fav;

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
        tv = findViewById(R.id.showLogHeader);


        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting up Bitmap Scaled Images
                int img = R.drawable.showevents;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                showimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Get all Event Years registered in Db
                Cursor res = new EventSqlliteDbService(getApplicationContext()).getYear();
                year.setAdapter(new EventsCF().setYears(getApplicationContext(), res));
                return null;
            }
        };

        Callable<Void> call3 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Life Events");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                tv.setText(content);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
        new CommonFunctions().setThreads(this, call3);
    }


    //Function to get dates from selected Year
   public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        String yr = year.getSelectedItem().toString();
       date.setAdapter(new EventsCF().setDates(this,new EventSqlliteDbService(this),yr,"normal"));
   }

    public void onNothingSelected(AdapterView<?> parent) { }


    //Function to get Event for Selected Date
    public void watchEvent(View v) {
        int fv = 0;
        Cursor res = new EventSqlliteDbService(this).getEvent(date.getSelectedItem().toString());
        while(res.moveToNext()){
            event.setText(res.getString(3));
            subject.setText(res.getString(2));
            fv = res.getInt(4);
        }
        if(fv == 1){ fav.setColorFilter(Color.RED);}
        else{ fav.setColorFilter(Color.BLACK);}
      }

}
