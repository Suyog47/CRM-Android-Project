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

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends Activity {
    String userCode, pass;
    EditText ucode;
    TextView hint, tv;
    ImageView loginimg;
    SpannableString content;
    private MediaPlayer mp;

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
        tv = findViewById(R.id.loginHeader);

        //Setting up new Thread to set Bitmap Scaled Images
        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int img = R.drawable.login;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                loginimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                content = new SpannableString("Enter the Access Code");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                tv.setText(content);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);

        //Code to underline the Text
        content = new SpannableString("Get Hint");
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
        hint.setText(content);

        pass = new CommonFunctions().getCache(this);
    }

    //Function to check valid Owner
    public void evaluate(View v){
        userCode = ucode.getText().toString();
        ucode.setText("");

        if(userCode.equals(pass)) {
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


    //Function to show Hint
    public void getHint(View v){
        String hint1 = "First Hint:- 01010011 | 01000001 | 0b0 | 0b1001";
        String hint2 = "Second Hint:- 0b1100100 | 01010010 | 0b1111101000 | 0b100 | 0b111";
        new AlertDialog.Builder(this)
                .setTitle("Decode this to get Code")
                .setMessage(hint1+"\n\n"+hint2)
                .show();
    }

}
