package com.uos.leitner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;
import com.uos.leitner.model.Sigmoid;
import com.uos.leitner.model.Subject_log;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

// 가로로 추가되는 ViewPager 생성
public class MainActivity extends AppCompatActivity implements CategoryView.Communicator {

    private DatabaseHelper db;
    private MyPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    FirebaseUser user;

    private DatabaseReference mSearchedLocationReference;


    static int MAX = 5; // 생성 가능한 페이지 수

    protected boolean flag = false; // false-> CategoryView 생성.  true-> MeasureView 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {

            // User isn't signed in
            finish();//exit current intent.

            //Intent intent = new Intent(this, LoginActivity.class);
            Intent intent = new Intent(this, IntroActivity.class);

            startActivity(intent);
        }

        db = new DatabaseHelper(getApplicationContext());

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(MAX);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();


        pagerAdapter.add(new VerticalActivity());   // MainActivity의 Adapter는 VerticalViewPager를 항목으로 가짐.

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private static final float thresholdOffset = 0.5f;
            private boolean scrollStarted, checkDirection;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (checkDirection) {
                    if (thresholdOffset > positionOffset) {
                        Log.e("scroll :", "right");
                    } else {
                        Log.e("scroll :", "left");
                    }
                    checkDirection = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                View view = viewPager.getChildAt(position);
                VerticalViewPager vvp = (VerticalViewPager)view.findViewById(R.id.vertical_pager);
                vvp.setCurrentItem(0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {   // 드레그 중
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        });
    }

    //어플에 메뉴 버튼 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list2, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    private void writeNewUser(String userId, String name, String email) {
//        User user = new User(name, email);
//
//        mDatabase.child("users").child(userId).setValue(user);
//    }

    //메뉴 버튼이 눌렸을 때의 이벤트 처리.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            // 백업 구현 완료!!.
            case R.id.backup:


                ref.child(user.getUid()).child("category").setValue(db.getAllCategories());
                ref.child(user.getUid()).child("subject_log").setValue(db.getAllSubject_log());
                ref.child(user.getUid()).child("sigmoid_log").setValue(db.getAllSigmoid());

//                writeNewUser(user.getUid(), username, user.getEmail());
                /*
                현재 SQLlite 정보를 json파일에 저장할 수 있도록 변환.

                Firebase DB에 저장.
                 */

                return true;

            case R.id.recover:

                //1. 기존 Table(Category, Subject_log) 내용삭제
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


                finish();//exit current intent.

                Intent intent = new Intent(this, MainActivity.class);

                startActivity(intent);
                

                return true;

            case R.id.logout:

                //logout related Firebase actions.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseAuth.getInstance().signOut();

                //logut related Google actions.

                /*
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount acct = result.getSignInAccount();
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                */


                startActivity(new Intent(MainActivity.this, LoginActivity.class)); //Go back to home page
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    if (id == R.id.action_log_out) {
//        ref.unauth(); //End user session
//
//    }


    //뒤로가기 클릭
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }

        else viewPager.setCurrentItem(0);
    }

    // CategoryView 인터페이스 구현
    @Override
    public  void initialize(ArrayList<Category> categoryList) {
        ArrayList<Category> cts = db.getAllCategories();

        for(Category c : cts) {
            categoryList.add(c);
        }

        if (!categoryList.isEmpty()) {
            for (int i = 0; i < categoryList.size(); i++) {
                Fragment fragment = VerticalActivity.newInstance(categoryList.get(i).getSubject_ID());
                pagerAdapter.add(fragment);
            }
        }
    }

    @Override
    public void showNext(int position) {
        //position 0번은 메인페이지
        viewPager.setCurrentItem(position + 1, true);
    }

    @Override
    public void delete(int position) {
        viewPager.removeViewAt(position + 1);
        pagerAdapter.remove(position + 1);
    }



}
