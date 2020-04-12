package com.example.demoapp.DailyActivityClasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class UpdateDailyActivity extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner week;
    Button ubtn;
    EditText act;
    ImageView updatedactivityimg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dailyactivity);

        week = findViewById(R.id.weekDropDown);
        week.setOnItemSelectedListener(this);
        act = findViewById(R.id.eventText);
        ubtn = findViewById(R.id.updateActBtn);
        updatedactivityimg = findViewById(R.id.updateDActivityImg);


        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting up Bitmap Scaled Image
                int img = R.drawable.updatedactivity;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                updatedactivityimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Get Current day sent from Previous Activity
                List<String> wk = new ArrayList<String>();
                wk.add("Sunday"); wk.add("Monday"); wk.add("Tuesday"); wk.add("Wednesday"); wk.add("Thursday"); wk.add("Friday"); wk.add("Saturday");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, wk);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                week.setAdapter(dataAdapter);
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

    //Function to get Current day activity
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        new CommonFunctions().showDActivity(this, new DActivitySqlliteDbService(this), week.getSelectedItem().toString(), act);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        new CommonFunctions().showDActivity(this, new DActivitySqlliteDbService(this), "Sunday", act);
    }

    //Function to update Current day activity
    public void updateActivity(View v) {
        boolean result = new DActivitySqlliteDbService(this).updateActivity(week.getSelectedItem().toString(), act.getText().toString());
        if (result == true) {
            ubtn.setText("Updated");
            ubtn.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ubtn.setText("Update Activity");
                    ubtn.setEnabled(true);
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
