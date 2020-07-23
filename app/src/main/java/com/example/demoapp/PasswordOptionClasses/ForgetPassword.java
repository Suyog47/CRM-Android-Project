package com.example.demoapp.PasswordOptionClasses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.R;

import java.util.concurrent.Callable;

import static android.content.ContentValues.TAG;

public class ForgetPassword extends Activity {

    ImageView fpimg;
    TextView fptv, emailtxt;
    EditText otp;
    int random_int;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        fpimg = findViewById(R.id.fpImg);
        fptv = findViewById(R.id.fpheader);
        otp = findViewById(R.id.setotp);
        emailtxt = findViewById(R.id.emailtxt);

        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Forget Password");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                fptv.setText(content);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int img = R.drawable.forgetpassword;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                fpimg.setImageBitmap(scaledImg);
                return null;
            }
        };

            emailtxt.setText(new CommonFunctions().getCache(getApplicationContext(), "email"));
            int min = 100000;
            int max = 999999;
            random_int = (int)(Math.random() * (max - min + 1) + min);
            SendEmail sm = new SendEmail(getApplicationContext(), emailtxt.getText().toString(), "Otp", "Your Otp is " + random_int);
            sm.execute();

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
    }

    public void checkOtp(View v){
        if(random_int == Integer.parseInt(otp.getText().toString())){
            Intent i1 = new Intent(this, PasswordChange.class);
            i1.putExtra("status",1);
            startActivity(i1);
        }
        else{
         otp.setError("Incorrect otp");
        }
    }
}
