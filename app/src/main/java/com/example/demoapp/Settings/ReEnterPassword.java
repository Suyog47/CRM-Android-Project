package com.example.demoapp.Settings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telecom.Call;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.PasswordOptionClasses.PasswordChange;
import com.example.demoapp.R;

import java.util.concurrent.Callable;

public class ReEnterPassword extends Activity {

    String ps;
    ImageView reimg;
    TextView retv;
    EditText code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_enterpassword);
        reimg = findViewById(R.id.reImg);
        retv = findViewById(R.id.reheader);
        code = findViewById(R.id.getcode);

        Callable<Void> call1 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Bundle bundle = getIntent().getExtras();
                ps = bundle.getString("pass");
                return null;
            }
        };

        Callable<Void> call2 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                SpannableString content = new SpannableString("Re-Checking Code");
                content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
                retv.setText(content);
                return null;
            }
        };

        Callable<Void> call3 = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int img = R.drawable.biograph;
                Display display = getWindowManager().getDefaultDisplay();
                Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
                reimg.setImageBitmap(scaledImg);
                return null;
            }
        };

        new CommonFunctions().setThreads(this, call1);
        new CommonFunctions().setThreads(this, call2);
        new CommonFunctions().setThreads(this, call3);
    }

    public void checkCode(View v){
        if(code.getText().toString().equals(ps)){
            Intent i1 = new Intent(this, PasswordChange.class);
            i1.putExtra("status",0);
            startActivity(i1);
        }
        else{
           code.setError("wrong Access Code");
        }
    }
}
