package com.example.demoapp.DatesClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.ImpDatesSqlliteDbService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Impdates extends Activity {

    Spinner date,month;
    EditText input;
    TextView t;
    LinearLayout mlayout, hlayout, vlayout;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impdates);
        date = new Spinner(this);
        month = new Spinner(this);
        input = new EditText(this);
        relativeLayout = findViewById(R.id.relativeLayout);

        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
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
                for(int i=1; i<=12; i++){
                    m.add(""+i);
                }
                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, m);
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                month.setAdapter(dataAdapter2);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                showDates(getApplicationContext());
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


    //Function to show Add date option in Alert Dialog
    public void showNoteDialog(View v){
        final View vv = v;
        try {
            mlayout = new LinearLayout(this);
            hlayout = new LinearLayout(this);
            vlayout = new LinearLayout(this);

            mlayout.setOrientation(LinearLayout.VERTICAL);
            hlayout.setOrientation(LinearLayout.HORIZONTAL);
            vlayout.setOrientation(LinearLayout.VERTICAL);

            final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(270, 120);
            final ViewGroup.LayoutParams mparams = new ViewGroup.LayoutParams(800, 180);

            date.setLayoutParams(lparams);
            date.setBackground(getResources().getDrawable(R.drawable.spinner));
            hlayout.addView(date);

            t = new TextView(this);
            t.setText("    |    ");
            t.setTextColor(Color.WHITE);
            t.setTextSize(25);
            hlayout.addView(t);

            month.setLayoutParams(lparams);
            month.setBackground(getResources().getDrawable(R.drawable.spinner));
            hlayout.addView(month);
            hlayout.setGravity(Gravity.CENTER);
            hlayout.setBackgroundColor(Color.BLACK);

            input.setLayoutParams(mparams);
            vlayout.setGravity(Gravity.CENTER);
            input.setTextColor(Color.WHITE);
            vlayout.addView(input);
            vlayout.setBackgroundColor(Color.BLACK);

            mlayout.addView(hlayout);
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
                                hlayout.removeAllViews();
                                vlayout.removeAllViews();
                                mlayout.removeAllViews();
                                showNoteDialog(vv);

                            } else {
                                checkFirst();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hlayout.removeAllViews();
                            vlayout.removeAllViews();
                            mlayout.removeAllViews();
                            input.setText(""); input.setHint("");
                        }
                    })
                    .show();
        }
        catch(Exception e){ }
    }


    //Function to check whether Event is already Inserted for Specified Date or not
    public void checkFirst(){
            String dt = date.getSelectedItem().toString() + "/" + month.getSelectedItem().toString();
            Cursor res = new ImpDatesSqlliteDbService(this).getCertainDate(dt);
            if (res.getCount() > 0) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("Already added Event for this Date")
                        .show();
                hlayout.removeAllViews();
                vlayout.removeAllViews();
                mlayout.removeAllViews();
                input.setText(""); input.setHint("");
            } else {
                newDate(dt);
            }
    }


    //Function to insert date-event
    public void newDate(String dt){
     String res = new ImpDatesSqlliteDbService(this).insertDate(dt, input.getText().toString());
     if(res == "Date Inserted"){
         hlayout.removeAllViews();
         vlayout.removeAllViews();
         mlayout.removeAllViews();
         input.setText(""); input.setHint("");
         showDates(this);
     }
     else{
         Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
         hlayout.removeAllViews();
         vlayout.removeAllViews();
         mlayout.removeAllViews();
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
