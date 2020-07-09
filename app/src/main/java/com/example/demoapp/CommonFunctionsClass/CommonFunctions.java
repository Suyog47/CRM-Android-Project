package com.example.demoapp.CommonFunctionsClass;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Display;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.DActivitySqlliteDbService;
import com.example.demoapp.SqlliteDBClasses.EventSqlliteDbService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.VIBRATOR_SERVICE;

public class CommonFunctions {

    private SoundPool soundPool;

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


    //Function to trigger Vibration
    public void setVibration(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(300);
        }
    }

    public void setThreads(Context context, Callable<Void> call1){
        List<Callable<Void>> tasklist = new ArrayList<>();
        tasklist.add(call1);

        ExecutorService executor = Executors.newCachedThreadPool();
        try{executor.invokeAll(tasklist);}
        catch(Exception e){
            Toast.makeText(context, "Something wrong in Threads", Toast.LENGTH_SHORT).show();}
        finally {
            executor.shutdown();
        }
    }

    public String setCache(Context context, byte[] text, String folder){
        String res;
        try{
            File cacheDir = context.getCacheDir();
            File file = new File(cacheDir.getAbsolutePath(),folder);
            FileOutputStream fs = new FileOutputStream(file);
            fs.write(text);
            fs.close();
            res = "success";
        }
        catch(Exception e){
            res = "something went wrong";
            e.printStackTrace();
        }
        return res;
    }

    public String getCache(Context context, String folder){
        String pass = null;
        try{
            File cacheDir = context.getCacheDir();
            File file = new File(cacheDir.getAbsolutePath(),folder);
            FileInputStream fs = new FileInputStream(file);
            StringBuffer sb  = new StringBuffer();

            int length;
            while ((length = fs.read()) > 0) {
                sb.append((char)length);
            }
            fs.close();
            pass = sb.toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return pass;
    }
}
