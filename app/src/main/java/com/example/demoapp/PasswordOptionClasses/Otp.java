package com.example.demoapp.PasswordOptionClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.demoapp.Login;
import com.example.demoapp.R;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Otp extends Activity {

    int random_int;
    TextView tv, otpmsg;
    EditText email, otp;
    Button chkbtn, sndbtn;
    ImageView otpimg;


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Otp.this)
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
        setContentView(R.layout.activity_otp);

        otpimg = findViewById(R.id.otpImg);
        tv = findViewById(R.id.otpheader);
        email = findViewById(R.id.setemail);
        otp = findViewById(R.id.setotp);
        chkbtn = findViewById(R.id.chkbtn);
        sndbtn = findViewById(R.id.sndbtn);
        otpmsg = findViewById(R.id.otpmsg);

        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Set Otp");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                tv.setText(content);
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int img = R.drawable.changepassword;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                otpimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
    }

    public void sendOtp(View v){
        String pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email.getText().toString());

        if(m.find()){
            ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if(isConnected) {
                int min = 100000;
                int max = 999999;
                random_int = (int) (Math.random() * (max - min + 1) + min);

                SendEmail sm = new SendEmail(this, email.getText().toString(), "Otp", "Your Otp is " + random_int);
                sm.execute();
                new CommonFunctions().setCache(this, email.getText().toString().getBytes(), "email");
                otpmsg.setVisibility(View.VISIBLE);
                email.setEnabled(false); sndbtn.setEnabled(false);
                otp.setEnabled(true); chkbtn.setEnabled(true);
            }
            else{
                new AlertDialog.Builder(Otp.this)
                        .setTitle("Oops...")
                        .setMessage("\t\t\t\t\t\t\tNo Internet Connection! \n\nTo change Password you should have an Active Internet Connection.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        })
                        .show();
            }
        }
        else{
            email.setError("put email in valid format");
        }

    }

    public void checkOtp(View v){
        int O = Integer.parseInt(otp.getText().toString());
        if(O == random_int){
          Intent i1 = new Intent(this, PasswordChange.class);
          i1.putExtra("status",1);
          startActivity(i1);
      }
      else{
         otp.setError("Wrong OTP");
      }
    }

    public void changeEmail(View v){
        email.setEnabled(true); sndbtn.setEnabled(true);
        otp.setEnabled(false); chkbtn.setEnabled(false);
        otp.setText("");
    }

}
