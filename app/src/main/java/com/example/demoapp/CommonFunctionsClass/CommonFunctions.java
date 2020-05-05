package com.example.demoapp.CommonFunctionsClass;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Display;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.demoapp.SqlliteDBClasses.DActivitySqlliteDbService;
import com.example.demoapp.SqlliteDBClasses.EventSqlliteDbService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.VIBRATOR_SERVICE;

public class CommonFunctions {

    //Function to set Image in BitmapFactory Class and get Scaled Image(short image)
    public Bitmap getScaledImage(Context context, int img, Display display){
        int displayWidth = display.getWidth();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), img, options);
        int width = options.outWidth;
        if(width > displayWidth){
            int widthRatio = Math.round((float) width / (float) displayWidth);
            options.inSampleSize = widthRatio;
        }
        options.inJustDecodeBounds = false;
        Bitmap scaledImg = BitmapFactory.decodeResource(context.getResources(), img, options);
        return scaledImg;
    }


    //Function to return year adapter
    public ArrayAdapter setYears(Context context, Cursor res){
        List<String> yr = new ArrayList<String>();
        if(res.getCount() == 0){ yr.add(""); }
        else{
            while(res.moveToNext()){ yr.add(res.getString(0));}
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, yr);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }


    //Function to return date adapter
    public ArrayAdapter setDates(Context context, EventSqlliteDbService db, String year, String type){
        List<String> d = new ArrayList<>();
        Cursor res;
        if(year != ""){
            if(type == "normal"){ res = db.getDates(Integer.parseInt(year)); }
            else{ res = db.getSweetDates(Integer.parseInt(year)); }

            if(res.getCount() == 0){ d.add(""); }
            else {
                while (res.moveToNext()) {
                    d.add(res.getString(1));
                }
            }
        }
        else{
            d.add("");
        }
        ArrayAdapter<String> da = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, d);
        da.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return da;
    }


    // Function to set daily-activities
    public void showDActivity(Context context, DActivitySqlliteDbService db, String message, TextView activity){
        db = new DActivitySqlliteDbService(context);
        Cursor act = db.getActivities(message);
        if(act.getCount() == 0){
            activity.setText("");
            activity.setHint("No Activities Registered");
        }
        else{
            while(act.moveToNext()){
                activity.setText(act.getString(1));
            }
        }
    }


    //Function to convert Bitmap Image to byte[]
    public byte[] convertToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }


    //Function to convert byte[] into Bitmap Image
    public static Bitmap convertToBitmapImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    //Function to trigger Vibration
    public void setVibration(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(300);
        }
    }

}
