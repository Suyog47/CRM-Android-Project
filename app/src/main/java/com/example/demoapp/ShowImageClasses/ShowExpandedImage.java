package com.example.demoapp.ShowImageClasses;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.CommonFunctionsClass.ShowImageCF;
import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.ShowImageDbService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                Bitmap img = new ShowImageCF().convertToBitmapImage(barr);
                ei.setImageBitmap(img);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
    }

}
