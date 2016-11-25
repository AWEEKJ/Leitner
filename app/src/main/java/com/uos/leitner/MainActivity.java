package com.uos.leitner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;

import java.util.ArrayList;

// 가로로 추가되는 ViewPager 생성
public class MainActivity extends AppCompatActivity implements CategoryView.Communicator {

    private DatabaseHelper db;
    private MyPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    static int MAX = 5; // 생성 가능한 페이지 수

    protected boolean flag = false; // false-> CategoryView 생성.  true-> MeasureView 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {

            // User isn't signed in
            finish();//exit current intent.

            Intent intent = new Intent(this, LoginActivity.class);

            startActivity(intent);
        }

        db = new DatabaseHelper(getApplicationContext());

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(MAX);

        pagerAdapter.add(new VerticalActivity());   // MainActivity의 Adapter는 VerticalViewPager를 항목으로 가짐.
    }

    //어플에 메뉴 버튼 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //메뉴 버튼이 눌렸을 때의 이벤트 처리.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.synchronize:

                /*


                 */

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
                finish();
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
