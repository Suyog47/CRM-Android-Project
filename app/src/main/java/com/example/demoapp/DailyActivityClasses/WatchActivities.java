package com.example.demoapp.DailyActivityClasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.CommonFunctionsClass.DailyActivitiesCF;
import com.example.demoapp.SqlliteDBClasses.DActivitySqlliteDbService;
import com.example.demoapp.R;
import java.util.concurrent.Callable;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class WatchActivities extends Activity {

    TextView title;
    TextView activity;
    ImageView watchactivityimg;
    Button ubtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_activity);
        title = findViewById(R.id.textView1);
        activity = findViewById(R.id.activityLabel);
        activity.setMovementMethod(new ScrollingMovementMethod());
        ubtn = findViewById(R.id.ubtn);
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
                new DailyActivitiesCF().showDActivity(getApplicationContext(), new DActivitySqlliteDbService(getApplicationContext()), message, activity);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
    }

    public void updateActivity(View v){
        if(ubtn.getText().toString().equals("update")){
            activity.setEnabled(true);
            activity.setFocusableInTouchMode(true);
            ubtn.setText("save");
        }
        else{
            boolean result = new DActivitySqlliteDbService(this).updateActivity(title.getText().toString(), activity.getText().toString());
            if (result == true) {
                ubtn.setText("Updated");
                ubtn.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ubtn.setText("update");
                        ubtn.setEnabled(true);
                        activity.setFocusable(false);
                    }
                }, 2000);
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops")
                        .setContentText("Activity Not Updated!")
                        .show();
            }
        }

    }
}
