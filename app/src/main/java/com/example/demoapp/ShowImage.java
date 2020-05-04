package com.example.demoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.SqlliteDBClasses.ShowImageDbService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;

public class ShowImage extends Activity {

    ImageView img;
    Bitmap bitmap;
    int count=0;
    float x1, x2;
    Cursor res;
    LinearLayout vlayout;
    RecyclerView recyclerView;
    ArrayList<Bitmap> imgList = new ArrayList();
    ArrayList<String> imgDate = new ArrayList();
    ArrayList<String> imgDesc = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                res = new ShowImageDbService(getApplicationContext()).getImages(count);
                showImages(res);
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


    //Function to show 6 Images on every count incr/decr
    public void showImages(Cursor res) {
            while (res.moveToNext()) {
                byte[] imgByte = res.getBlob(0);
                imgList.add(new CommonFunctions().convertToBitmapImage(imgByte));
                imgDate.add(res.getString(1));
                imgDesc.add(res.getString(2));
            }
            recyclerView = findViewById(R.id.imageRecyclerView);

            ShowImageListAdapter adapter = new ShowImageListAdapter(getApplicationContext(), imgList, imgDate, imgDesc);
            recyclerView.setHasFixedSize(true);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recyclerView.setAdapter(adapter);
    }


    //Function to increment Count
    public void showNext(View v){
        count+=6;
        res = new ShowImageDbService(getApplicationContext()).getImages(count);
        if(res.getCount() >= 1){
            imgList.clear(); imgDate.clear(); imgDesc.clear();
            showImages(res);
        }
        else{ count-=6; }
    }


    //Function to decrement Count
    public void showPrevious(View v){
        if(count > 0){
            imgList.clear(); imgDate.clear(); imgDesc.clear();
            count-=6;
            res = new ShowImageDbService(getApplicationContext()).getImages(count);
            showImages(res);
        }
    }


    //Function to open Phone Galary
    public void openGalary(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 & resultCode == RESULT_OK && data != null && data.getData() != null){
           openDialog(data);
        }
    }


    //Function to open Dialog containing selected image
    public void openDialog(Intent data){
        img = new ImageView(this);
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(920, 520);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        img.setLayoutParams(lparams);

        TextView tv = new TextView(this);
        tv.setText("----------------------");
        tv.setTextSize(30);
        tv.setGravity(Gravity.CENTER);

        final EditText txt = new EditText(this);

        vlayout = new LinearLayout(this);
        vlayout.setOrientation(LinearLayout.VERTICAL);
        vlayout.addView(img);
        vlayout.addView(tv);
        vlayout.addView(txt);

        Uri uri = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            img.setImageBitmap(bitmap);
        }
        catch(IOException e){
            Toast.makeText(this,"something wrong",Toast.LENGTH_LONG).show(); }

        new AlertDialog.Builder(this)
                .setTitle("Selected Image")
                .setView(vlayout)
                .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkFirst(txt.getText().toString());
                    }
                })
                .show();
    }


    //Function to check if Image with same Description exists or not
    public void checkFirst(String text){
         Cursor res = new ShowImageDbService(this).getSelectedImage(text);
         if(res.getCount() > 0){
             new CommonFunctions().setVibration(this);
             new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                     .setTitleText("Already wrote this description for another Image")
                     .setContentText("write something different")
                     .show();
         }
         else{ addImage(bitmap, text); }
    }


    //Function to insert Image into db
    public void addImage(Bitmap bitmapImg, String desc){
        Date dt = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       byte[] byteImg = new CommonFunctions().convertToByteArray(bitmapImg);
       String res = new ShowImageDbService(this).insertImages(byteImg, desc, dateFormat.format(dt));
        imgList.clear();
        imgDate.clear();
        imgDesc.clear();
       Cursor res1 = new ShowImageDbService(getApplicationContext()).getImages(count);
       showImages(res1);
       Toast.makeText(this, res, Toast.LENGTH_LONG).show();
    }


}
