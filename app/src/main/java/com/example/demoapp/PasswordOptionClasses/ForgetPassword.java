package com.example.demoapp.PasswordOptionClasses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.R;

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

        SpannableString content = new SpannableString("Forget Password");
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
        fptv.setText(content);

        int img = R.drawable.changepassword;
        Display display = getWindowManager().getDefaultDisplay();
        Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
        fpimg.setImageBitmap(scaledImg);

        emailtxt.setText(new CommonFunctions().getCache(this, "email"));

        int min = 100000;
        int max = 999999;
        random_int = (int)(Math.random() * (max - min + 1) + min);

        SendEmail sm = new SendEmail(this, emailtxt.getText().toString(), "Otp", "Your Otp is " + random_int);
        sm.execute();
    }

    public void checkOtp(View v){
        if(random_int == Integer.parseInt(otp.getText().toString())){
            Intent i1 = new Intent(this, PasswordChange.class);
            startActivity(i1);
        }
        else{
         otp.setError("Incorrect otp");
        }
    }
}
