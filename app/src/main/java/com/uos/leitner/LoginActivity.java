package com.uos.leitner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/*
 * Created by JungJee on 2016. 10. 24..
 */

public class LoginActivity extends AppCompatActivity
implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Button login_btn = (Button) findViewById(R.id.login_button);
//        login_btn.setOnClickListener(this);
//
//        Button signup_email_btn = (Button) findViewById(R.id.sign_in_email_button);
//        signup_email_btn.setOnClickListener(this);

        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.sign_in_email_button).setOnClickListener(this);
        findViewById(R.id.sign_in_google_button).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int i = view.getId();

        if (i == R.id.sign_in_email_button) {
            Intent intent = new Intent(this, SigninActivity.class);

            startActivity(intent);
        } else if(i == R.id.login_button){
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
        } else if(i == R.id.sign_in_google_button){

            Intent intent = new Intent(this, GoogleSigninActivity.class);

            startActivity(intent);
        }
    }

}
