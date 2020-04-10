package com.example.demoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.BackgroundProcessClass.BackupAndRestoreDBs;
import com.example.demoapp.CommonFunctionsClass.CommonFunctions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends Activity {
    String userCode;
    EditText ucode;
    TextView hint;
    ImageView loginimg;
    private MediaPlayer mp;
    CommonFunctions cf = new CommonFunctions();
    BackupAndRestoreDBs res = new BackupAndRestoreDBs();

    //Showing Alert Dialog to Confirm the Exit of Application
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Login.this)
                .setTitle("Are you Sure!")
                .setMessage("Do you want to close this App")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .show();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ucode = findViewById(R.id.ucode);
        hint = findViewById(R.id.hint);
        loginimg = findViewById(R.id.loginImg);

        //Setting up new Thread to set Bitmap Scaled Images
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int img = R.drawable.login;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = cf.getScaledImage(getApplicationContext(), img, display);
                loginimg.setImageBitmap(scaledImg);
                return null;
            }
        };

       List<Callable<Void>> tasklist = new ArrayList<>();
       tasklist.add(call1);

        ExecutorService executor = Executors.newCachedThreadPool();
        try{executor.invokeAll(tasklist);}
        catch(Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}
        finally {
            executor.shutdown();
        }

        //Code to underline the Text
        SpannableString content = new SpannableString("Get Hint");
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
        hint.setText(content);
    }

    //Function to check valid Owner
    public void evaluate(View v){
        userCode = ucode.getText().toString();
        if(userCode.equals("SA09")) {
            Intent a1 = new Intent(this,MenuData.class);
            startActivity(a1);
        }
        else{
            mp = MediaPlayer.create(this, R.raw.errorsound);
            if (!mp.isPlaying()) {
                mp.setVolume(75,75);
                mp.start();
            }
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                  .setTitleText("Nope...Wrong Access Code")
                  .show();
        }
    }

    //Function to set Link like Text
    public void getHint(View v){
        String hint1 = "First Hint:- 01010011 | 01000001 | 0b0 | 0b1001";
        String hint2 = "Second Hint:- 0b1100100 | 01010010 | 0b1111101000 | 0b100 | 0b111";
        new AlertDialog.Builder(this)
                .setTitle("Decode this to get Hint")
                .setMessage(hint1+"\n"+hint2)
                .show();
    }

}
