package com.example.demoapp.NotesClasses;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.NotesSqlliteDbService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Notes extends Activity {

    TextView[] note = new TextView[100];
    TextView tv;
    LinearLayout lLayout;
    LinearLayout.LayoutParams[] params = new LinearLayout.LayoutParams[100];
    NotesSqlliteDbService db;
    ImageView loginimg;
    int i = 0, id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        lLayout = findViewById(R.id.notesLayout);
        loginimg = findViewById(R.id.loginImg);
        tv = findViewById(R.id.notesHeader);

        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting up Bitmap Scaled Image
                int img = R.drawable.login;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                loginimg.setImageBitmap(scaledImg);
                return null;
            }
        };

       Callable<Void> call2 = new Callable<Void>() {
           @Override
           public Void call() throws Exception {
               showNotes();
               return null;
           }
       };

       Callable<Void> call3 = new Callable<Void>() {
           @Override
           public Void call() throws Exception {
               SpannableString content = new SpannableString("Notes");
               content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
               tv.setText(content);
               return null;
           }
       };

       List<Callable<Void>> taskList = new ArrayList<>();
        taskList.add(call1);
        taskList.add(call2);
        taskList.add(call3);

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

    //Function to Show all the Notes in programmatically made layouts
    public void showNotes(){
        db = new NotesSqlliteDbService(this);
        Cursor val = db.getNotes();

        while (val.moveToNext()) {
            note[i] = new TextView(this);
            note[i].setId(i);
            note[i].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200
            ));
            params[i] = (LinearLayout.LayoutParams) note[i].getLayoutParams();
            note[i].setText(val.getString(1));
            if(val.getString(2).equals("Important")){ note[i].setTextColor(Color.GREEN); }
            else if(val.getString(2).equals("Extreme")) { note[i].setTextColor(Color.RED); }
            note[i].setTextSize(21);
            note[i].setPadding(20,20,20,20);
            params[i].setMargins(100, 20, 100, 50);
            note[i].setBackground(getResources().getDrawable(R.drawable.noteviewdesign));
            note[i].setLayoutParams(params[i]);
            note[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    seeFullNote(v);
                }
            });
            note[i].setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    deleteNote(v);
                    return true;
                }
            });
            lLayout.addView(note[i]);
            i += 1;
        }
    }


    //Function to start AddNote Activity
    public void newNote(View v){
        Intent a1 = new Intent(this, AddNotes.class);
        startActivity(a1);
    }


    //Function to Expand the specified layout to see Complete Note
    public void seeFullNote(View v){
        id = v.getId();
        ViewGroup.LayoutParams param = note[id].getLayoutParams();
        int lines;
        if(note[id].getMeasuredHeight() == 200){
            lines = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        else{ lines = 200; }

        param.height = lines;
        param.width = 880;
        note[id].setLayoutParams(param);
        }


        //Function to delete specified Note
        public void deleteNote(View v){
            id = v.getId();
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.noteanim);
            note[id].startAnimation(animation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    note[id].setVisibility(View.GONE);
                }
            }, 500);

            db = new NotesSqlliteDbService(this);
            int res = db.deleteNote(note[id].getText().toString());
            if(res == 0){
                new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops")
                        .setContentText("Note cannot be Deleted!")
                        .show();
            }
        }
}

