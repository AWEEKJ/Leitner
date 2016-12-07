package com.uos.leitner.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.uos.leitner.R;

/**
 * Created by HANJU on 2016. 12. 7..
 */

public class SettingListActivity extends AppCompatActivity {

    static final String[] LIST_MENU = {"백업", "불러오기", "로그아웃"} ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_list);

        // Hide toolbar text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        ListView listview = (ListView) findViewById(R.id.settingList) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;

                // TODO : use strText
                if (strText == LIST_MENU[0]) {

                } else if (strText == LIST_MENU[1]) {

                } else if (strText == LIST_MENU[2]) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), IntroLoginActivity.class)); //Go back to home page
                }
            }
        }) ;


    }
}
