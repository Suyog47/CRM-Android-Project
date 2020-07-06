package com.example.demoapp.Bio;

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
import com.example.demoapp.PasswordOptionClasses.PasswordChange;
import com.example.demoapp.R;

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

        Bundle bundle = getIntent().getExtras();
        ps = bundle.getString("pass");

        SpannableString content = new SpannableString("Re-Checking Code");
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
        retv.setText(content);

        int img = R.drawable.forgetpassword;
        Display display = getWindowManager().getDefaultDisplay();
        Bitmap scaledImg = new CommonFunctions().getScaledImage(getApplicationContext(), img, display);
        reimg.setImageBitmap(scaledImg);
    }

    public void checkCode(View v){
        if(code.getText().toString().equals(ps)){
            Intent i1 = new Intent(this, PasswordChange.class);
            startActivity(i1);
        }
        else{
           code.setError("wrong Access Code");
        }
    }
}
