package com.example.demoapp.DailyActivityClasses;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.R;

public class DailyActivity extends Activity {
    String s1;
    Button b1,b2,b3,b4,b5,b6,b7,btn1,btn2,btn3,btn4,btn5,btn6,btn7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyactivity);

        b1 = findViewById(R.id.set_Sunday_Timer); b2 = findViewById(R.id.set_Monday_Timer); b3 = findViewById(R.id.set_Tuesday_Timer);
        b4 = findViewById(R.id.set_Wednesday_Timer); b5 = findViewById(R.id.set_Thursday_Timer); b6 = findViewById(R.id.set_Friday_Timer);
        b7 = findViewById(R.id.set_Saturday_Timer);

        btn1 = findViewById(R.id.see_Sunday); btn2 = findViewById(R.id.see_Monday); btn3 = findViewById(R.id.see_Tuesday);
        btn4 = findViewById(R.id.see_Wednesday); btn5 = findViewById(R.id.see_Thursday); btn6 = findViewById(R.id.see_Friday);
        btn7 = findViewById(R.id.see_Saturday);
    }


    //Function to Start Timer Activity
    public void setTimer(View v){
        int id = v.getId();
        String idString = "no id";
        if(id != View.NO_ID) {
            Resources res = v.getResources();
            if(res != null)
                idString = res.getResourceEntryName(id);
        }
        idString = idString.split("_")[1];

        Intent a1 = new Intent(this,  SetTimer.class);
        a1.putExtra("title", idString);
        startActivity(a1);
    }


    //Function to See Activity
    public void seeActivity(View v){
        int id = v.getId();
        String idString = "no id";
        if(id != View.NO_ID){
            Resources res = v.getResources();
            if(res != null){
                idString = res.getResourceEntryName(id);
            }
        }
        idString = idString.split("_")[1];

       Intent a2 = new Intent(this,  WatchActivities.class);
       a2.putExtra("title", idString);
       startActivity(a2);
    }
}
