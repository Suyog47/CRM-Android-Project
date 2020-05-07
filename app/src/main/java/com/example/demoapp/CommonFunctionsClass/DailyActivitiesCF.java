package com.example.demoapp.CommonFunctionsClass;

import android.content.Context;
import android.database.Cursor;
import android.widget.TextView;

import com.example.demoapp.SqlliteDBClasses.DActivitySqlliteDbService;

public class DailyActivitiesCF {

    // Function to set daily-activities
    public void showDActivity(Context context, DActivitySqlliteDbService db, String message, TextView activity){
        db = new DActivitySqlliteDbService(context);
        Cursor act = db.getActivities(message);
        if(act.getCount() == 0){
            activity.setText("");
            activity.setHint("No Activities Registered");
        }
        else{
            while(act.moveToNext()){
                activity.setText(act.getString(1));
            }
        }
    }
}
