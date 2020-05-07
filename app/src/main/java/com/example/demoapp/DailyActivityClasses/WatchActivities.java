package com.example.demoapp.DailyActivityClasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.SqlliteDBClasses.DActivitySqlliteDbService;
import com.example.demoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WatchActivities extends Activity {

    TextView title;
    TextView activity;
    ImageView watchactivityimg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_activity);
        title = findViewById(R.id.textView1);
        activity = findViewById(R.id.activityLabel);
        activity.setMovementMethod(new ScrollingMovementMethod());
        watchactivityimg = findViewById(R.id.watchActivityImg);

        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting up Bitmap Scaled Image
                int img = R.drawable.seeactivity;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                watchactivityimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Get current Day
                Bundle bundle = getIntent().getExtras();
                String message = bundle.getString("title");
                SpannableString content = new SpannableString(message);
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                title.setText(content);

                //Get Current day Activity
                new CommonFunctions().showDActivity(getApplicationContext(), new DActivitySqlliteDbService(getApplicationContext()), message, activity);
                return null;
            }
        };

        List<Callable<Void>> taskList = new ArrayList<>();
        taskList.add(call1);
        taskList.add(call2);

        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            Toast.makeText(this, "Something wrong in Threads", Toast.LENGTH_SHORT).show();
        }
        finally {
            executor.shutdown();
        }
    }
}
