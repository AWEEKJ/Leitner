package com.uos.leitner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
 * Created by JungJee on 2016. 10. 24..
 */

public class LoginActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, SigninActivity.class);

        startActivity(intent);
    }
}
