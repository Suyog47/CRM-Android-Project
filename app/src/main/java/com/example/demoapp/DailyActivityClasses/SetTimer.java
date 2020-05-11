package com.example.demoapp.DailyActivityClasses;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.BackgroundProcessClass.BackgroundNotificationService;
import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.SqlliteDBClasses.DActivitySqlliteDbService;
import com.example.demoapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SetTimer extends Activity {

    String message;
    TimePicker timePicker;
    TextView title;
    Button btn;
    Switch timer;
    int Hr = 0, Min = 0;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
    ImageView setimerimg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settimer);
        timePicker = findViewById(R.id.timePicker);
        title = findViewById(R.id.dayText);
        btn = findViewById(R.id.setTimerBtn);
        timer = findViewById(R.id.timerSwitch);
        setimerimg = findViewById(R.id.setTimerImg);


        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting up Bitmap Scaled Image
                int img = R.drawable.settimer;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                setimerimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Get Current day sent from Previous Activity
                Bundle bundle = getIntent().getExtras();
                message = bundle.getString("title");
                SpannableString content = new SpannableString(message);
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                title.setText(content);
                showSetTimer();
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
    }


    //Function to get the Timer for current day(if set)
    public void showSetTimer(){
        Cursor dt = new DActivitySqlliteDbService(getApplicationContext()).getTimer(message);
        String dtimer = null;
        boolean stat = false;
        int dtimerstatus = 0;

        while(dt.moveToNext()){
            dtimer = dt.getString(0);
            dtimerstatus = dt.getInt(1);
        }
        if(dtimer == null){ timer.setVisibility(View.GONE);}
        else{
            timer.setVisibility(View.VISIBLE);
            timer.setText(dtimer);
            stat = (dtimerstatus == 1) ? true : false;
            timer.setChecked(stat);
        }
    }


    //Function to Update timer
    public void setTimer(View v) {
        String time = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
        boolean result = new DActivitySqlliteDbService(getApplicationContext()).updateTimer(title.getText().toString(), time);
        if (result == true) {
            btn.setText("Timer Placed");
            btn.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn.setText("Set Timer");
                    btn.setEnabled(true);
                }
            }, 2000);
        }
        else{
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops")
                    .setContentText("Timer not Set!")
                    .show();
        }
        showSetTimer();
        setDayMinus1();
    }


    //Function to switch the status of timer(Whether On or Off)
    public void switchTimer(View v){
        boolean status = timer.isChecked();
        int res = (status) ? 1 : 0;
        boolean result = new DActivitySqlliteDbService(getApplicationContext()).setTimerStatus(title.getText().toString(), res);
        if(result == true){
            Toast.makeText(this, "Timer Set "+status, Toast.LENGTH_SHORT).show();
            setDayMinus1();
        }
        else{ Toast.makeText(this, "Something Went Wrong...Timer not Set!", Toast.LENGTH_SHORT).show(); }
    }


    //Function to set Calendar Instance date to Yesterday's date
    public void setDayMinus1(){
        Date dt = new Date();
        String time;
        String Day = formatter.format(dt);
        if(Day.equals(title.getText().toString())){
            time = timer.getText().toString();
            Hr = Integer.parseInt(time.split(":")[0]);
            Min = Integer.parseInt(time.split(":")[1]);
            if(Hr >= Integer.parseInt(new SimpleDateFormat("hh").format(dt)) && Min > Integer.parseInt(new SimpleDateFormat("mm").format(dt))){
                calendar.add(Calendar.DAY_OF_YEAR, -1);
            }
        }
        placeTimer(this);
    }


    //Function to Start the timer
    public void placeTimer(Context context){
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String Day = formatter.format(calendar.getTime());
        String time = null;

        Cursor dt = new DActivitySqlliteDbService(context).getActivities(Day);
        while(dt.moveToNext()){
            time = dt.getString(2);
        }
        if(time != null){
            Hr = Integer.parseInt(time.split(":")[0]);
            Min = Integer.parseInt(time.split(":")[1]);
        }
        else{
            Hr = 10;
            Min = 00;
        }

        calendar.set(Calendar.HOUR_OF_DAY, Hr);
        calendar.set(Calendar.MINUTE, Min);
        calendar.set(Calendar.SECOND, 00);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent(context, BackgroundNotificationService.class), 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
