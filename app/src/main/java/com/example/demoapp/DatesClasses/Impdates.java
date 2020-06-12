package com.example.demoapp.DatesClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.ImpDatesSqlliteDbService;

import java.util.ArrayList;
import java.util.concurrent.Callable;


import cn.pedant.SweetAlert.SweetAlertDialog;

public class Impdates extends Activity {

    int d, m;
    EditText input;
    TextView t, tv;
    LinearLayout mlayout, hlayout, vlayout;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impdates);
        input = new EditText(this);
        relativeLayout = findViewById(R.id.relativeLayout);
        tv = findViewById(R.id.impdatesHeader);


        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                showDates(getApplicationContext());
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Important Dates");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                tv.setText(content);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
    }


    //Function to show Add date option in Alert Dialog
    public void showNoteDialog(View v){
        final View vv = v;
        try {
            mlayout = new LinearLayout(this);
            vlayout = new LinearLayout(this);

            mlayout.setOrientation(LinearLayout.VERTICAL);
            vlayout.setOrientation(LinearLayout.VERTICAL);

            final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final ViewGroup.LayoutParams mparams = new ViewGroup.LayoutParams(800, 180);

            final DatePicker dp = new DatePicker(this);
            dp.setLayoutParams(lparams);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                d = dayOfMonth;
                m = month+1;
                }
            });

            input.setLayoutParams(mparams);
            vlayout.setGravity(Gravity.CENTER);
            vlayout.addView(dp);
            vlayout.addView(input);

            mlayout.addView(vlayout);

            new AlertDialog.Builder(this)
                    .setTitle("            Add New Note")
                    .setView(mlayout)
                    .setPositiveButton("Add New", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (TextUtils.isEmpty(input.getText().toString())) {
                                input.setHint("Please Write Something");
                                input.setHintTextColor(Color.RED);
                                vlayout.removeAllViews(); mlayout.removeAllViews();
                                showNoteDialog(vv);
                            } else {
                                checkFirst(d, m);
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            vlayout.removeAllViews(); mlayout.removeAllViews();
                            input.setText(""); input.setHint("");
                        }
                    })
                    .show();
        }
        catch(Exception e){ }
    }


    //Function to check whether Event is already Inserted for Specified Date or not
    public void checkFirst(int date, int month){
            String dt = date + "/" + month;
            Cursor res = new ImpDatesSqlliteDbService(this).getCertainDate(dt);
            if (res.getCount() > 0) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("Already added Event for this Date")
                        .show();
                vlayout.removeAllViews(); mlayout.removeAllViews();
                input.setText(""); input.setHint("");
            } else {
                newDate(dt);
            }
    }


    //Function to insert date-event
    public void newDate(String dt){
     String res = new ImpDatesSqlliteDbService(this).insertDate(dt, input.getText().toString());
     if(res == "Date Inserted"){
         vlayout.removeAllViews(); mlayout.removeAllViews();
         input.setText(""); input.setHint("");
         showDates(this);
     }
     else{
         Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
         vlayout.removeAllViews(); mlayout.removeAllViews();
         input.setText(""); input.setHint("");
     }
    }


    //Function to get all dates and its events
    public void showDates(Context context){
        Cursor res = new ImpDatesSqlliteDbService(context).getDates();
        ArrayList Dates = new ArrayList<>();
        ArrayList Events = new ArrayList<>();
        if(res.getCount() == 0){ }
        else{
            while(res.moveToNext()){
                Dates.add(res.getString(0));
                Events.add(res.getString(1));
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ImpDatesListAdapter adapter = new ImpDatesListAdapter(context, Dates, Events);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}
