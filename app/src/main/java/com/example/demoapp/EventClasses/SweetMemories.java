package com.example.demoapp.EventClasses;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
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

public class SweetMemories extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner year, date;
    TextView event, subject, tv;
    ImageView sweetmemimg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweet_memories);

        date = findViewById(R.id.dateDropDown);
        year = findViewById(R.id.yearDropDown);
        subject = findViewById(R.id.subjectLabel);
        subject.setMovementMethod(new ScrollingMovementMethod());
        event = findViewById(R.id.eventLabel);
        event.setMovementMethod(new ScrollingMovementMethod());
        year.setOnItemSelectedListener(this);
        sweetmemimg = findViewById(R.id.sweetMemImg);
        tv = findViewById(R.id.sweetmemHeader);


        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting up Bitmap Scaled Images
                int img = R.drawable.sweetmem;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                sweetmemimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Get favorite Event Years registered in Db
                Cursor res = new EventSqlliteDbService(getApplicationContext()).getSweetYears();
                year.setAdapter(new EventsCF().setYears(getApplicationContext(), res));
                return null;
            }
        };

        Callable<Void> call3  = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Sweet Memories");
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
        try{ executor.invokeAll(taskList); }
        catch(InterruptedException ie){ Toast.makeText(this, "Something wrong in Threads", Toast.LENGTH_SHORT).show(); }
        finally{ executor.shutdown(); }

    }


    //Function to get dates from selected Year
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        String yr = year.getSelectedItem().toString();
        date.setAdapter(new EventsCF().setDates(this, new EventSqlliteDbService(this), yr,"sweet"));
    }

    public void onNothingSelected(AdapterView<?> parent) { }


    //Function to get Event for Selected Date
    public void watchEvent(View v) {
        Cursor res = new EventSqlliteDbService(this).getEvent(date.getSelectedItem().toString());
        while(res.moveToNext()){
            event.setText(res.getString(3));
            subject.setText(res.getString(2));}
    }

}
