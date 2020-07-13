package com.example.demoapp.PasswordOptionClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.Login;
import com.example.demoapp.R;

import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordChange extends Activity {

    int status;
    TextView tv;
    EditText p1, p2;
    ImageView passimg;

    @Override
    public void onBackPressed() {
        if(status == 1) {
            new AlertDialog.Builder(PasswordChange.this)
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
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        tv = findViewById(R.id.passheader);
        p1 = findViewById(R.id.setcode);
        p2 = findViewById(R.id.confirmcode);
        passimg = findViewById(R.id.passwordImg);

        Bundle bundle = getIntent().getExtras();
        status = bundle.getInt("status");

        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Change Password");
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
                passimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
    }

    public void setPassword(View v){
        if(p1.getText().toString().equals(p2.getText().toString())){
            String pattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#%*,]).{4,32}$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(p1.getText().toString());
            if(m.find()){
                String res = new CommonFunctions().setCache(this, p2.getText().toString().getBytes(), "pass");
                p1.setText(""); p2.setText("");
                if(res.equals("success")) {
                    Toast.makeText(this, "Password Updates", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                p1.setError("match the given password format");
            }

        }
        else{
            p2.setError("confirm password does not match");
        }
    }

    public void goLogin(View v){
        Intent i1 = new Intent(this, Login.class);
        startActivity(i1);
    }
}
