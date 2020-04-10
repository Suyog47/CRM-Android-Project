package com.example.demoapp.BackgroundProcessClass;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.example.demoapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

public class BackupAndRestoreDBs extends Activity {

    RelativeLayout rl;

    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    File Dir = new File(path+"/CRM");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
        rl = findViewById(R.id.backupRelativeLayout);
        rl.setVisibility(View.INVISIBLE);

                authenticateAgain();
    }


    //Function to show alert dialog to authenticate user
    private void authenticateAgain() {
        LinearLayout l = new LinearLayout(this);
        final ViewGroup.LayoutParams mparams = new ViewGroup.LayoutParams(400, 180);
        final EditText pass = new EditText(this);
        pass.setLayoutParams(mparams);
        l.addView(pass);
        l.setGravity(Gravity.CENTER);

        new AlertDialog.Builder(this)
                .setTitle("          Enter Pass Code")
                .setView(l)
                .setPositiveButton("Check", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(pass.getText().toString().equals("CRM47")){
                            Log.i(TAG,"SUCCESS");
                            rl.setVisibility(View.VISIBLE);
                            successCall();
                        }
                         else {
                            authenticateAgain();
                        }
                    }
                }).show();
    }

    public void successCall(){
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                isStoragePermissionGranted();
                return null;
            }
        };
        List<Callable<Void>> task = new ArrayList<>();
        task.add(call1);

        ExecutorService executor = Executors.newCachedThreadPool();

        try { executor.invokeAll(task); }
        catch (InterruptedException ie) { Toast.makeText(this, "Something wrong in Threads", Toast.LENGTH_SHORT).show(); }
        finally{ executor.shutdown(); }
    }


    //Function to ask the External Storage permission
    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Log.v(TAG,"Permission Requested");
            }
        else {
            Log.v(TAG,"Permission is granted by Default");
        }
    }


    //Function to grant permission if clicked "yes"
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ " was "+grantResults[0]);

        }
    }

    public void backUp(View v){
        Dir.mkdirs();
        backupDB("Memory.db");
        backupDB("DActivity.db");
        backupDB("Notes.db");
        backupDB("Dates.db");

        Toast.makeText(this,"All Database has been Backuped",Toast.LENGTH_LONG).show();
    }


    //Function to Backup the Db onto Phone's Storage
    public void backupDB(String name){
        OutputStream output;
        FileInputStream fis = null;
        try {
            String fileName = getApplicationContext().getDatabasePath(name).getPath();
            File file = new File(fileName);
            fis = new FileInputStream(file);
        }
        catch(Exception e){ e.printStackTrace(); }

        try {
            String outFileName = Environment.getExternalStorageDirectory() + "/CRM/" + name;
            output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            fis.close();
        }
        catch(Exception e){ e.printStackTrace(); }
    }

    public void reStore(View v){
        restoreDB("Memory.db");
        restoreDB("DActivity.db");
        restoreDB("Notes.db");
        restoreDB("Dates.db");

        Toast.makeText(this,"All Database has been Restored",Toast.LENGTH_LONG).show();
    }


    //Function to Reload the Db from Phone's Storage
    public void restoreDB(String name) {
        OutputStream output;
        FileInputStream fis = null;
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/CRM/" + name;
            File file = new File(fileName);
            fis = new FileInputStream(file);
        } catch (Exception e) {
            Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG).show();
        }

        try {
            String outFileName = getApplicationContext().getDatabasePath(name).getPath();
            output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Function to Upload db to Cloud Server
    public void Upload(View v){
      Toast.makeText(this,"Uploading Code not written yet",Toast.LENGTH_LONG).show();
    }


    //Function to Download db from Cloud Server
    public void Download(View v){
        Toast.makeText(this,"Downloading Code not written yet",Toast.LENGTH_LONG).show();

    }

}
