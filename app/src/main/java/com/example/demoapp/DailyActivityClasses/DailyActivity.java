package com.example.demoapp.DailyActivityClasses;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import com.example.demoapp.R;


public class DailyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyactivity);
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
