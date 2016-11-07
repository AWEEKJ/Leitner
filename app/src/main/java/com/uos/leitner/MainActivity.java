package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryView.Communicator {
    protected int MAX = 5;   // 생성가능한 페이지 갯수

    private DatabaseHelper db;
    private MyPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_pager);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(MAX);

        pagerAdapter.add(new VerticalActivity());   // 처음 메인화면 생성
    }

    //뒤로가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) super.onBackPressed();

        else viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    //CategoryView의 인터페이스를 구현해서 정보교환

    //1. DB로부터 초기화
    @Override
    public  void initialize(ArrayList<Category> categoryList) {

        ArrayList<Category> cts = db.getAllCategories();
        categoryList = cts;

        if (!categoryList.isEmpty()) {
            for (int i = 0; i < categoryList.size(); i++) {
                Fragment fragment = VerticalActivity.newInstance(categoryList.get(i).getSubject_Name());
                pagerAdapter.add(fragment);
            }
        }
    }

    //2. 세부항목별 페이지 생성
    @Override
    public void addCategory(ArrayList<Category> categoryList, int position) {
        if (pagerAdapter.getCount() <= 10) {
            Log.d("TEST", Integer.toString(position));
            Fragment fragment = VerticalActivity.newInstance(categoryList.get(position-1).getSubject_Name());

            pagerAdapter.add(fragment);
        }
    }

    //3. 리스트 클릭하면 해당 페이지로 이동
    @Override
    public void showNext(int position) {
        //position 0번은 메인페이지
        viewPager.setCurrentItem(position + 1, true);
    }

    @Override
    public void InsertDB(Category contents) {
        db.createCategory(contents);
    }
}
