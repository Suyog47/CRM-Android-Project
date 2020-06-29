package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;

import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {

    String pass;
    ImageView sLogo;
    ProgressBar start;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sLogo = findViewById(R.id.smallLogo);
        sLogo.setColorFilter(Color.WHITE);
        start = findViewById(R.id.progressBar);

        pass = new CommonFunctions().getCache(this);

        //Starting Login Activity after 2400ms.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(pass == null) {
                    Intent i = new Intent(MainActivity.this, PasswordChange.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                }
                finish();
            }
        }, 2000);


        //Setting Display Screen to Full Screen and Removing Navigation Bar.
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
