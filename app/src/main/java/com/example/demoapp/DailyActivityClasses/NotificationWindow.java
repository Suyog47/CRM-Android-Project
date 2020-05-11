package com.example.demoapp.DailyActivityClasses;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.CommonFunctionsClass.DailyActivitiesCF;
import com.example.demoapp.SqlliteDBClasses.DActivitySqlliteDbService;
import com.example.demoapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NotificationWindow extends Activity {

    TextView event;
    String FullDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_window);
        event = findViewById(R.id.eventLabel);

        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Get current Day
                Date dt = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
                FullDay = formatter.format(dt);

                //Get Current day Activity
                new DailyActivitiesCF().showDActivity(getApplicationContext(), new DActivitySqlliteDbService(getApplicationContext()), FullDay, event);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
    }
}
