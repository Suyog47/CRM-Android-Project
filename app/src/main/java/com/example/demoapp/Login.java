package com.example.demoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
import com.example.demoapp.PasswordOptionClasses.ForgetPassword;

import java.io.DataOutputStream;
import java.util.concurrent.Callable;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends Activity {
    String userCode, pass;
    EditText ucode;
    TextView fp, tv;
    ImageView loginimg;
    SpannableString content;
    SoundPool soundPool;
    private int soundID;


    //Showing Alert Dialog to Confirm the Exit of Application
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Login.this)
                .setTitle("Are you Sure!")
                .setMessage("Do you want to close this App")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       moveTaskToBack(true);
                       android.os.Process.killProcess(android.os.Process.myPid());
                       System.exit(1);
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
        fp = findViewById(R.id.forgetpassword);
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
        content = new SpannableString("Forgot Password?");
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
        fp.setText(content);

        pass = new CommonFunctions().getCache(this, "pass");
        setSound();
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
//            mp = MediaPlayer.create(this, R.raw.errorsound);
//            if (!mp.isPlaying()) {
//                mp.setVolume(75,75);
//                mp.start();
//            }

            soundPool.play(soundID,1,1,1,0,1);

            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                  .setTitleText("Nope...Wrong Access Code")
                  .show();
        }
    }


    //Function to show Hint
    public void forgetPassword(View v){
       Intent i1 = new Intent(this, ForgetPassword.class);
       startActivity(i1);
    }


    public void setSound(){
        AudioAttributes audio = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audio)
                .build();

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC , 0);
        soundID = soundPool.load(this, R.raw.errorsound,1);
    }
}
