package com.example.demoapp;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.SqlliteDBClasses.ShowImageDbService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.Inflater;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ShowExpandedImage extends Activity {

    byte[] barr;
    ImageView ei;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expandedimage);
        ei = findViewById(R.id.expandedImage);

        Bundle bundle = getIntent().getExtras();
        final String ImgDesc = bundle.getString("imgDesc");

        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Cursor res = new ShowImageDbService(getApplicationContext()).getSelectedImage(ImgDesc);
                while (res.moveToNext()) {
                    barr = res.getBlob(0);
                }
                Bitmap img = new CommonFunctions().convertToBitmapImage(barr);
                ei.setImageBitmap(img);
                return null;
            }
        };

        List<Callable<Void>> taskList = new ArrayList<>();
        taskList.add(call1);

        ExecutorService executor = Executors.newCachedThreadPool();
        try { executor.invokeAll(taskList); }
        catch (InterruptedException ie) { Toast.makeText(this, "Something wrong in Threads", Toast.LENGTH_SHORT).show(); }
        finally{ executor.shutdown(); }
    }

}
