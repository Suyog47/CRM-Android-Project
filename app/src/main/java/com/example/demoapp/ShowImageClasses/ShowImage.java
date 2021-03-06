package com.example.demoapp.ShowImageClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import com.example.demoapp.CommonFunctionsClass.ShowImageCF;
import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.ShowImageDbService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ShowImage extends Activity {

    ImageView img, bgimg;
    TextView tv;
    Bitmap bitmap, photo;
    int count=0;
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
        tv = findViewById(R.id.imagesHeader);
        bgimg = findViewById(R.id.showImageImg);

        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                res = new ShowImageDbService(getApplicationContext()).getImages(count);
                showImages(res);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Images");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                tv.setText(content);
                return null;
            }
        };

        Callable<Void> call3 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int img = R.drawable.showimagebg;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                bgimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
        new CommonFunctions().setThreads(this, call3);
    }


    //Function to show 6 Images on every count incr/decr
    public void showImages(Cursor res) {
            while (res.moveToNext()) {
                byte[] imgByte = res.getBlob(0);
                imgList.add(new ShowImageCF().convertToBitmapImage(imgByte));
                imgDate.add(res.getString(1));
                imgDesc.add(res.getString(2));
            }
            recyclerView = findViewById(R.id.imageRecyclerView);

            ShowImageListAdapter adapter = new ShowImageListAdapter(getApplicationContext(), imgList, imgDate, imgDesc);
            recyclerView.setHasFixedSize(true);
           // StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            res = new ShowImageDbService(getApplicationContext()).getImages(count)
            ;
            showImages(res);
        }
    }


    //Function to open Phone Galary
    public void openGalary(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,1);
    }

    //Function to open Phone Camera
    public void openCamera(View v){
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 & resultCode == RESULT_OK && data != null && data.getData() != null){
           openDialog(data, 1);
        }

        if(requestCode == 2){
            if(resultCode != RESULT_CANCELED) {
                photo = (Bitmap) data.getExtras().get("data");
                openDialog(null, 2);
            }
        }
    }


    //Function to open Dialog containing selected image
    public void openDialog(Intent data, final int requestCode){
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

        if(requestCode == 1) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, "something wrong", Toast.LENGTH_LONG).show();
            }
        }
        else{
            img.setImageBitmap(photo);
        }
        new AlertDialog.Builder(this)
                .setTitle("Selected Image")
                .setView(vlayout)
                .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkFirst(txt.getText().toString(), requestCode);
                    }
                })
                .show();
    }


    //Function to check if Image with same Description exists or not
    public void checkFirst(String text, int rc){
         Cursor res = new ShowImageDbService(this).getSelectedImage(text);
         if(res.getCount() > 0){
             new CommonFunctions().setVibration(this);
             new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                     .setTitleText("Already wrote this description for another Image")
                     .setContentText("write something different")
                     .show();
         }
         else{
             if(rc == 1){
                 addImage(bitmap, text);
             }
             if(rc == 2){
                 addImage(photo, text);
             }
         }
    }


    //Function to insert Image into db
    public void addImage(Bitmap bitmapImg, String desc){
        Date dt = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       byte[] byteImg = new ShowImageCF().convertToByteArray(bitmapImg);
       String res = new ShowImageDbService(this).insertImages(byteImg, desc, dateFormat.format(dt));
       imgList.clear();
       imgDate.clear();
       imgDesc.clear();
       Cursor res1 = new ShowImageDbService(getApplicationContext()).getImages(count);
       showImages(res1);
       Toast.makeText(this, res, Toast.LENGTH_LONG).show();
    }

}
