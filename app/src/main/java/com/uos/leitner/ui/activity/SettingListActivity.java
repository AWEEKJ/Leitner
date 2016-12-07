package com.uos.leitner.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.uos.leitner.R;
import com.uos.leitner.database.DatabaseHelper;

/**
 * Created by HANJU on 2016. 12. 7..
 */

public class SettingListActivity extends AppCompatActivity {

    static final String[] LIST_MENU = {"Backup to cloud", "Sync from cloud", "Logout"} ;

    DatabaseHelper db;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseUser user;
    DatabaseReference mSearchedLocationReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_list);

        // Hide toolbar text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        db = new DatabaseHelper(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_setting_list, R.id.item_setting, LIST_MENU) ;

        ListView listview = (ListView) findViewById(R.id.settingList) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;

                // TODO : use strText
                if (strText == LIST_MENU[0]) {
                    ref.child(user.getUid()).child("category").setValue(db.getAllCategories());
                    ref.child(user.getUid()).child("subject_log").setValue(db.getAllSubject_log());
                    ref.child(user.getUid()).child("sigmoid_log").setValue(db.getAllSigmoid());

                } else if (strText == LIST_MENU[1]) {

                    ref.child(user.getUid()).getRef();
                    ref.child(user.getUid()).getDatabase();

                    mSearchedLocationReference = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child(user.getUid());

                } else if (strText == LIST_MENU[2]) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(getApplicationContext(), IntroLoginActivity.class)); //Go back to home page
                }
            }
        }) ;


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
