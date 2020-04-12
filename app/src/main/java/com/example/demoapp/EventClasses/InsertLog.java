package com.example.demoapp.EventClasses;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.SqlliteDBClasses.EventSqlliteDbService;
import com.example.demoapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InsertLog extends Activity {

    TextView year;
    Spinner date,month;
    EditText subject, event;
    Button btn;
    ImageButton fbtn;
    Boolean fav = false;
    float x1, x2;
    int MIN_DISTANCE = 50;
    Date dt = new Date();
    MediaPlayer player;
    SimpleDateFormat formatter = new SimpleDateFormat("YYYY");
    ImageView insertimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        date = findViewById(R.id.dateDropDown);
        month = findViewById(R.id.monthDropDown);
        btn = findViewById(R.id.insertLogBtn);
        fbtn = findViewById(R.id.favorite);
        subject = findViewById(R.id.subjectText);
        event = findViewById(R.id.eventText);
        insertimg = findViewById(R.id.insertImg);

        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {

                //Setting up Bitmap Scaled Image
                int img = R.drawable.insertevent;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                insertimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting dates to Dropdown1
                List<String> d = new ArrayList<>();
                for(int i=1; i<=31; i++){
                    d.add(""+i);
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, d);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                date.setAdapter(dataAdapter);

                //Setting months to Dropdown2
                List<String> m = new ArrayList<>();
                m.add("Jan");m.add("Feb");m.add("Mar");m.add("Apr");m.add("May");m.add("Jun");m.add("Jul");m.add("Aug");m.add("Sep");m.add("Oct");m.add("Nov");m.add("Dec");

                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, m);
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                month.setAdapter(dataAdapter2);

                //Setting Current year to TextView
                year = findViewById(R.id.yearText);
                year.setText(formatter.format(dt));
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


    //Code to Identify Swipe gestures from right to left and vise-versa
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    if (x2 > x1)
                    {
                        int yr = Integer.parseInt(year.getText().toString());
                        yr-=1;
                        year.setText(yr+"");
                    }
                    else
                    {
                        int yr = Integer.parseInt(year.getText().toString());
                        yr+=1;
                        year.setText(yr+"");
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    //Function to set Event as Favorite or not
    public void fav(View v){
        fav = !fav;
        if(fav == true){fbtn.setColorFilter(Color.RED);}
        else{fbtn.setColorFilter(Color.BLACK);}

        player = MediaPlayer.create(this, R.raw.ticksound);
        if(!player.isPlaying()) {
            player.setVolume(75,75);
            player.start();
        }
    }


    //Function to Validate Input
    public void validateInput(View v){
        if(TextUtils.isEmpty(subject.getText().toString())){
            subject.setError("Subject should be filled");
        }
        else if(TextUtils.isEmpty(event.getText().toString())){
            event.setError("Event should be filled");
        }
        else {
            String fullDate = date.getSelectedItem().toString() + "/" + month.getSelectedItem().toString() + "/" + year.getText().toString();
            checkFirst(fullDate);
        }
    }


    //Function to check whether Event is already Inserted for Specified Date or not
    public void checkFirst(String fullDate){
        Cursor res = new EventSqlliteDbService(this).getEvent(fullDate);
        if(res.getCount() >= 1){
            new CommonFunctions().setVibration(this);
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Already Inserted for this Date")
                    .setContentText("Try updating ur Event")
                    .show();
        }
        else{ insert(fullDate); }
    }


    //Function to Insert Event
    public void insert(String fullDate){
        String result = new EventSqlliteDbService(this).insertData(Integer.parseInt(year.getText().toString()), fullDate, subject.getText().toString(), event.getText().toString(), fav);
        if(result == "Data Inserted!") {
            btn.setText("Event Inserted");
            btn.setEnabled(false);
            event.setText("");
            subject.setText("");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn.setText("Log Event");
                    btn.setEnabled(true);
                }
            }, 2000);
        }
        else
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops")
                    .setContentText(result)
                    .show();
        }
    }

}
