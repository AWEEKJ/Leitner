package com.uos.leitner.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.uos.leitner.R;
import com.uos.leitner.database.DatabaseHelper;
import com.uos.leitner.model.Category;
import com.uos.leitner.model.Subject_log;

import java.util.List;

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

                    // 1. 기존 Table(Category, Subject_log) 내용삭제
                    db.clearTables();

                    //2. insertBackupData from firebase
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<List<Category>> t = new GenericTypeIndicator<List<Category>>() {};
                            List<Category> categories = dataSnapshot.child(user.getUid()).child("category").getValue(t);

                            GenericTypeIndicator<List<Subject_log>> l = new GenericTypeIndicator<List<Subject_log>>() {};
                            List<Subject_log> logs = dataSnapshot.child(user.getUid()).child("subject_log").getValue(l);

                            Log.e("test", ""+dataSnapshot.child(user.getUid()).child("subject_log"));
                            Log.e("test", ""+dataSnapshot.child(user.getUid()).child("category"));
                            for(Category c : categories) {
                                db.createCategory(c);
                            }

                            for(Subject_log slog : logs) {
                                db.createSubjectLog(slog);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("onCancelled", "Fail");
                        }
                    });

                    finish();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else if (strText == LIST_MENU[2]) {
                    FirebaseAuth.getInstance().signOut();
                    finishAffinity();
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
