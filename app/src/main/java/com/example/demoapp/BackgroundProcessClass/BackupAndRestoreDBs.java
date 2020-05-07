package com.example.demoapp.BackgroundProcessClass;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
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


public class BackupAndRestoreDBs extends Activity {

    RelativeLayout rl;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    File Dir = new File(path+"/CRM");
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
        rl = findViewById(R.id.backupRelativeLayout);

        tv = findViewById(R.id.dbHeader);

        SpannableString content = new SpannableString("Database Controls");
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
        tv.setText(content);

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
            }
    }


    //Function to grant permission if clicked "yes"
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission granted for Storage", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Permission not given for Storage", Toast.LENGTH_LONG).show();
        }
    }


    public void backUp(View v){
        Dir.mkdirs();
        backupDB("Memory.db");
        backupDB("DActivity.db");
        backupDB("Notes.db");
        backupDB("Dates.db");
        backupDB("Image.db");

        Toast.makeText(this,"All Database has been Backuped",Toast.LENGTH_LONG).show();
    }


    //Function to Backup the Db onto Phone's Storage
    public void backupDB(String name){
        OutputStream output;
        FileInputStream fis = null;
        try {
            String filePath = getApplicationContext().getDatabasePath(name).getPath();
            File file = new File(filePath);
            fis = new FileInputStream(file);
        }
        catch(Exception e){ e.printStackTrace(); }

        try {
            String outFilePath = Environment.getExternalStorageDirectory() + "/CRM/" + name;
            output = new FileOutputStream(outFilePath);

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
        restoreDB("Image.db");

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
