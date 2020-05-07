package com.example.demoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.BackgroundProcessClass.BackupAndRestoreDBs;
import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.DailyActivityClasses.DailyActivity;
import com.example.demoapp.DailyActivityClasses.UpdateDailyActivity;
import com.example.demoapp.DatesClasses.Impdates;
import com.example.demoapp.EventClasses.InsertLog;
import com.example.demoapp.EventClasses.ShowLog;
import com.example.demoapp.EventClasses.SweetMemories;
import com.example.demoapp.EventClasses.UpdateLog;
import com.example.demoapp.NotesClasses.Notes;
import com.example.demoapp.ShowImageClasses.ShowImage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuData extends Activity {

    TextView tv;
    ImageView menuimg;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_data);
        menuimg = findViewById(R.id.menuImg);
        tv = findViewById(R.id.menuHeader);

        SpannableString content = new SpannableString("Activities");
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
        tv.setText(content);

        //Setting up new Thread to set Bitmap Scaled Images
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int img = R.drawable.menu;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                menuimg.setImageBitmap(scaledImg);
                return null;
            }
        };
        setThreads(call1);

    }

    //Function to start Insert Log Activity
    public void insertLog(View v){
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent al = new Intent(getApplicationContext(), InsertLog.class);
                startActivity(al);
                return null;
            }
        };
        setThreads(call1);
    }

    //Function to start Show Log Activity
    public void showLog(View v) {
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent a2 = new Intent(getApplicationContext(), ShowLog.class);
                startActivity(a2);
                return null;
            }
        };
        setThreads(call1);
    }

    //Function to start Daily-Activities Activity
    public void dailyActivity(View v){
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent a3 = new Intent(getApplicationContext(), DailyActivity.class);
                startActivity(a3);
                return null;
            }
        };
        setThreads(call1);
    }

    //Function to start Sweet-Memory Activity
    public void sweetMemories(View v){
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent a4 = new Intent(getApplicationContext(), SweetMemories.class);
                startActivity(a4);
                return null;
            }
        };
        setThreads(call1);
    }

    //Function to start Update Log Activity
    public void updateLog(View v){
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent a5 = new Intent(getApplicationContext(), UpdateLog.class);
                startActivity(a5);
                return null;
            }
        };
        setThreads(call1);
    }

    //Function to start Update daily-activities Activity
    public void updateDailyActivity(View v){
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent a6 = new Intent(getApplicationContext(), UpdateDailyActivity.class);
                startActivity(a6);
                return null;
            }
        };
        setThreads(call1);
    }

    //Function to start Notes Activity
    public void notes(View v){
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent a7 = new Intent(getApplicationContext(), Notes.class);
                startActivity(a7);
                return null;
            }
        };
        setThreads(call1);

    }

    //Function to start ImpDates Activity
    public void impdates(View v){
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent a8 = new Intent(getApplicationContext(), Impdates.class);
                startActivity(a8);
                return null;
            }
        };
        setThreads(call1);
    }

    //Function to start ShowImages Activity
    public void showImage(View v){
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Intent a9 = new Intent(getApplicationContext(), ShowImage.class);
                startActivity(a9);
                return null;
            }
        };
        setThreads(call1);
    }

    //Function to start BackUp and Restore Activity
    public void dbcontrols(final View v){
        LinearLayout l = new LinearLayout(this);
        final ViewGroup.LayoutParams mparams = new ViewGroup.LayoutParams(400, 180);
        final EditText pass = new EditText(this);
        pass.setLayoutParams(mparams);
        pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        l.addView(pass);
        l.setGravity(Gravity.CENTER);

        new AlertDialog.Builder(this)
                .setTitle("Enter Pass Code")
                .setView(l)
                .setPositiveButton("Check", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(pass.getText().toString().equals("CRM47")){
                            Callable<Void> call1 = new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    Intent a10 = new Intent(getApplicationContext(), BackupAndRestoreDBs.class);
                                    startActivity(a10);
                                    return null;
                                }
                            };
                            setThreads(call1);
                        }
                        else {
                            dbcontrols(v);
                        }
                    }
                }).show();
    }


    public void setThreads(Callable<Void> call1){
        List<Callable<Void>> tasklist = new ArrayList<>();
        tasklist.add(call1);

        ExecutorService executor = Executors.newCachedThreadPool();
        try{executor.invokeAll(tasklist);}
        catch(Exception e){
            Toast.makeText(this, "Something wrong in Threads", Toast.LENGTH_SHORT).show();}
        finally {
            executor.shutdown();
        }
    }
}
