package com.example.demoapp.Settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.R;

import java.util.concurrent.Callable;

public class Biograph extends Activity {

    String pass;
    ImageView bioimg;
    TextView biotv;
    EditText emltxt, passtxt;
    Button emlbtn, passbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biograph);
        bioimg = findViewById(R.id.bioImg);
        biotv = findViewById(R.id.bioheader);
        emltxt = findViewById(R.id.emltxt);
        passtxt = findViewById(R.id.passtxt);
        emlbtn = findViewById(R.id.emlbtn);
        passbtn = findViewById(R.id.passbtn);

        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Biograph");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                biotv.setText(content);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int img = R.drawable.biograph;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                bioimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        Callable<Void> call3 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                pass = new CommonFunctions().getCache(getApplicationContext(),"pass");
                emltxt.setText(new CommonFunctions().getCache(getApplicationContext(),"email"));
                passtxt.setText("************");
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
        new CommonFunctions().setThreads(this, call3);
    }

    public void changeEmail(View v){
        if(emlbtn.getText().toString().equals("Change Email")){
            emltxt.setFocusableInTouchMode(true);
            emlbtn.setText("Update");
        }
        else{
            String res = new CommonFunctions().setCache(this, emltxt.getText().toString().getBytes(),"email");
            if(res.equals("success")) {
                Toast.makeText(this, "Email Updates", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
            }
            emltxt.setFocusable(false);
            emlbtn.setText("Change Email");
        }
    }

    public void updatePass(View v){
        Intent i1 = new Intent(this, ReEnterPassword.class);
        i1.putExtra("pass", pass);
        startActivity(i1);
        passtxt.setText("************");
    }

    public void watch(View v){
        passtxt.setText(pass);
    }
}
