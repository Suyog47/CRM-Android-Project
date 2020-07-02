package com.example.demoapp.PasswordOptionClasses;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Otp extends Activity {

    int random_int;
    TextView tv;
    EditText email, otp;
    Button chkbtn, sndbtn;
    ImageView otpimg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otpimg = findViewById(R.id.otpImg);
        tv = findViewById(R.id.otpheader);
        email = findViewById(R.id.setemail);
        otp = findViewById(R.id.setotp);
        chkbtn = findViewById(R.id.chkbtn);
        sndbtn = findViewById(R.id.sndbtn);

        SpannableString content = new SpannableString("Set Otp");
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
        tv.setText(content);

        int img = R.drawable.changepassword;
        Display display = getWindowManager().getDefaultDisplay();
        Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
        otpimg.setImageBitmap(scaledImg);
    }

    public void sendOtp(View v){
        String pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email.getText().toString());

        if(m.find()){
            int min = 100000;
            int max = 999999;
            random_int = (int)(Math.random() * (max - min + 1) + min);

            SendEmail sm = new SendEmail(this, email.getText().toString(), "Otp", "Your Otp is " + random_int);
            sm.execute();
            new CommonFunctions().setCache(this, email.getText().toString().getBytes(), "email");
            email.setEnabled(false); sndbtn.setEnabled(false);
            otp.setEnabled(true);  chkbtn.setEnabled(true);
        }
        else{
            email.setError("put email in valid format");
        }

    }

    public void checkOtp(View v){
        int O = Integer.parseInt(otp.getText().toString());
        if(O == random_int){
          Intent i1 = new Intent(this, PasswordChange.class);
          startActivity(i1);
      }
      else{
          Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show();
      }
    }

}
