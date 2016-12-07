package com.uos.leitner.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.uos.leitner.R;
import com.uos.leitner.database.DatabaseHelper;
import com.uos.leitner.model.Category;
import com.uos.leitner.ui.adapter.CategoryListPagerAdapter;
import com.uos.leitner.ui.fragment.CategoryListFragment;
import com.uos.leitner.ui.fragment.CategoryListVerticalPagerFragment;

import java.util.ArrayList;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

// 가로로 추가되는 ViewPager 생성
public class MainActivity extends AppCompatActivity implements CategoryListFragment.Communicator {

    private DatabaseHelper db;
    private CategoryListPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    FirebaseUser user;

    private DatabaseReference mSearchedLocationReference;

    static int MAX = 5; // 생성 가능한 페이지 수

    public static int getMAX() {
        return MAX;
    }

    public boolean flag = false; // false-> CategoryListFragment 생성.  true-> MeasureFragment 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide toolbar text
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {

            // User isn't signed in
            finish();//exit current intent.

            Intent intent = new Intent(this, IntroActivity.class);

            startActivity(intent);
        }

        db = new DatabaseHelper(getApplicationContext());

        pagerAdapter = new CategoryListPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(MAX);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();


        pagerAdapter.add(new CategoryListVerticalPagerFragment());   // MainActivity의 Adapter는 VerticalViewPager를 항목으로 가짐.

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

//      //메뉴 버튼이 눌렸을 때의 이벤트 처리.
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        // Handle presses on the action bar items
////        switch (item.getItemId()) {
////
////            case R.id.backup:
////
////                ref.child(user.getUid()).child("category").setValue(db.getAllCategories());
////                ref.child(user.getUid()).child("subject_log").setValue(db.getAllSubject_log());
////                ref.child(user.getUid()).child("sigmoid_log").setValue(db.getAllSigmoid());
////
////                return true;
////
////            case R.id.recover:
////                ref.child(user.getUid()).getRef();
////                ref.child(user.getUid()).getDatabase();
////
////                mSearchedLocationReference = FirebaseDatabase
////                        .getInstance()
////                        .getReference()
////                        .child(user.getUid());
////
////                return true;
////
////            case R.id.logout:
////                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////                FirebaseAuth.getInstance().signOut();
////
////                startActivity(new Intent(MainActivity.this, IntroLoginActivity.class)); //Go back to home page
////                return true;
////            default:
////                return super.onOptionsItemSelected(item);
////        }
//    }

    //뒤로가기 클릭
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }

        else viewPager.setCurrentItem(0);
    }

    // CategoryListFragment 인터페이스 구현
    @Override
    public  void initialize(ArrayList<Category> categoryList) {
        ArrayList<Category> cts = db.getAllCategories();

        for(Category c : cts) {
            categoryList.add(c);
        }

        if (!categoryList.isEmpty()) {
            for (int i = 0; i < categoryList.size(); i++) {
                Fragment fragment = CategoryListVerticalPagerFragment.newInstance(categoryList.get(i).getSubject_ID());
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

    // 어플에 메뉴 버튼 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
