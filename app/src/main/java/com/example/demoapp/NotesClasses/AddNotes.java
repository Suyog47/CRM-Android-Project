package com.example.demoapp.NotesClasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.NotesSqlliteDbService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddNotes extends Activity {

    Spinner flag;
    EditText notes;
    Button noteBtn;
    Date dt = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    NotesSqlliteDbService db;
    ImageView addnotesimg;
    CommonFunctions cf = new CommonFunctions();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnotes);

        flag = findViewById(R.id.flags);
        notes = findViewById(R.id.NotesText);
        noteBtn = findViewById(R.id.addNoteBtn);
        addnotesimg = findViewById(R.id.addNotesImg);

        //Setting up new Thread
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //Setting up Bitmap Scaled Image
                int img = R.drawable.notesbg2;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = cf.getScaledImage(getApplicationContext(), img, display);
                addnotesimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                //set values to Dropdown
                List<String> fl = new ArrayList<>();
                fl.add("Basic");
                fl.add("Important");
                fl.add("Extreme");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, fl);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                flag.setAdapter(dataAdapter);
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


    //Function to add new Note
    public void addNewNote(View v) {
        if (TextUtils.isEmpty(notes.getText().toString())) {
            notes.setError("Write Something");
        } else {
            db = new NotesSqlliteDbService(this);
            String date = formatter.format(dt);
            String note = notes.getText().toString();
            String flg = flag.getSelectedItem().toString();
            String res = db.insertNote(date, note, flg);

            if (res == "Notes Inserted") {
                notes.setText("");
                noteBtn.setText("Note Inserted");
                noteBtn.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notes.setText("");
                        noteBtn.setText("Add Note");
                        noteBtn.setEnabled(true);
                    }
                }, 2000);
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops")
                        .setContentText(res)
                        .show();
            }
        }
    }
}
