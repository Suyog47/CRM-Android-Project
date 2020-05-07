package com.example.demoapp.EventClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Call;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.CommonFunctionsClass.EventsCF;
import com.example.demoapp.SqlliteDBClasses.EventSqlliteDbService;
import com.example.demoapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UpdateLog extends Activity implements AdapterView.OnItemSelectedListener {

    int fv=0;
    Spinner year, date;
    Button ubtn, dbtn;
    EditText subject, event;
    ImageView updateventimg, fav;
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatelog);

        year = findViewById(R.id.yearDropDown);
        date = findViewById(R.id.dateDropDown);
        year.setOnItemSelectedListener(this);
        ubtn = findViewById(R.id.updateBtn);
        dbtn = findViewById(R.id.deleteBtn);
        subject = findViewById(R.id.subjectText);
        event = findViewById(R.id.eventText);
        fav = findViewById(R.id.fav);
        updateventimg = findViewById(R.id.updateEventImg);
        tv = findViewById(R.id.updateLogHeader);

        subject.setEnabled(false); event.setEnabled(false); ubtn.setEnabled(false); fav.setEnabled(false);

        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting up Bitmap Scaled Images
                int img = R.drawable.updatevent;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                updateventimg.setImageBitmap(scaledImg);
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
                SpannableString content = new SpannableString("Update Event");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                tv.setText(content);
                return null;
            }
        };

        List<Callable<Void>> taskList = new ArrayList<>();
        taskList.add(call1);
        taskList.add(call2);
        taskList.add(call3);

        ExecutorService executor =  Executors.newCachedThreadPool();
        try{ executor.invokeAll(taskList); }
        catch (InterruptedException ie) { Toast.makeText(this, "Something wrong in Threads", Toast.LENGTH_SHORT).show(); }
        finally{ executor.shutdown(); }
    }


    //Function to get dates from selected Year
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String yr = year.getSelectedItem().toString();
        date.setAdapter(new EventsCF().setDates(this, new EventSqlliteDbService(this), yr,"normal"));
    }

    public void onNothingSelected(AdapterView<?> parent) { }


    //Function to get Event for Selected Date
    public void watchEvent(View v){
        subject.setEnabled(true); event.setEnabled(true); ubtn.setEnabled(true); fav.setEnabled(true);

        Cursor res = new EventSqlliteDbService(this).getEvent(date.getSelectedItem().toString());
        while(res.moveToNext()){
            subject.setText(res.getString(2));
            event.setText(res.getString(3));
            fv = res.getInt(4);
        }
        if(fv == 1){ fav.setColorFilter(Color.RED);}
        else{ fav.setColorFilter(Color.BLACK);}
    }

    //Function to set/change Event's fav status
    public void setFav(View v){
        if(fv == 1){ fav.setColorFilter(Color.BLACK); fv = 0;}
        else{ fav.setColorFilter(Color.RED); fv = 1;}
    }


    //Function to validate input
    public void validateInput(View v) {
        if(TextUtils.isEmpty(subject.getText().toString())){
           subject.setError("Subject should be filled");
        }
        else if(TextUtils.isEmpty(event.getText().toString())){
            event.setError("Event should be filled");
        }
        else {
            update();
        }
    }


    //Function to update Event
    public void update(){
        Boolean favrt = (fv == 1) ? true : false;
        boolean result = new EventSqlliteDbService(this).updateEvent(date.getSelectedItem().toString(), subject.getText().toString(), event.getText().toString(), favrt);
        if (result == true) {
            ubtn.setText("Event Updated");
            ubtn.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ubtn.setText("Update");
                    ubtn.setEnabled(true);
                }
            }, 2000);
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops")
                    .setContentText("Data not Updated Bro!")
                    .show();
        }
    }


    //Function to ask for Confirmation for deletion
    public void deleteEvent(View v) {
        new CommonFunctions().setVibration(this);
        new AlertDialog.Builder(this)
                .setTitle("Are You Sure!")
                .setMessage("You Want to Delete this Event?")
                .setPositiveButton("Yes Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sureDelete();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .show();
    }


    //Function to delete event
    public void sureDelete(){
        String dte = date.getSelectedItem().toString();
        int result = new EventSqlliteDbService(this).deleteEvent(dte);
        if (result > 0) {
            String yr = year.getSelectedItem().toString();
            date.setAdapter(new EventsCF().setDates(this, new EventSqlliteDbService(this), yr,"normal"));
                subject.setText("");
                event.setText("");
                dbtn.setText("Event Deleted");
                dbtn.setEnabled(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    event.setText("");
                    subject.setText("");
                    dbtn.setText("Delete");
                    dbtn.setEnabled(true);
                }
            }, 2000);
        }
        else{
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops")
                    .setContentText("Data not Deleted Bro!")
                    .show();
        }
    }

}
