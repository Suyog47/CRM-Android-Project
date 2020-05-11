package com.example.demoapp.DailyActivityClasses;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DailyActivity extends Activity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyactivity);
        tv = findViewById(R.id.dactivityHeader);

        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Daily Activities");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                tv.setText(content);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
    }


    //Function to Start Timer Activity
    public void setTimer(View v){
        int id = v.getId();
        String idString;
        Resources res = v.getResources();
        idString = res.getResourceEntryName(id).split("_")[1];

        Intent a1 = new Intent(this,  SetTimer.class);
        a1.putExtra("title", idString);
        startActivity(a1);
    }


    //Function to See Activity
    public void seeActivity(View v){
        int id = v.getId();
        String idString;
        Resources res = v.getResources();
        idString = res.getResourceEntryName(id).split("_")[1];

       Intent a2 = new Intent(this,  WatchActivities.class);
       a2.putExtra("title", idString);
       startActivity(a2);
    }
}
