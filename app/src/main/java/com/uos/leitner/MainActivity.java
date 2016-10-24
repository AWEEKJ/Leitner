package com.uos.leitner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.uos.leitner.R.layout.login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Intent intent = new Intent(this, login.class);
        //startActivity(intent);
        //setContentView(R.layout.activity_main);
    }

    public void onClick(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }
}
