package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryView.Communicator {
    protected int MAX = 5;   // 생성가능한 페이지 갯수
    private MyPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    //1. 세부항목별 페이지 생성
    @Override
    public void addCategory(ArrayList<Category> categoryList, int position) {
        if (pagerAdapter.getCount() <= 10) {
            Fragment fragment = VerticalActivity.newInstance(categoryList.get(position-1).getName());

            pagerAdapter.add(fragment);
        }
    }

    @Override
    public  void initialize(ArrayList<Category> categoryList) {
        Category a = new Category("공부", "1시간");
        Category b = new Category("명상", "2시간");
        Category c = new Category("독서", "3시간");

        categoryList.add(a);
        categoryList.add(b);
        categoryList.add(c);

        for(int i=0; i<categoryList.size(); i++) {
            Fragment fragment = VerticalActivity.newInstance(categoryList.get(i).getName());
            pagerAdapter.add(fragment);
        }
    }



    //2. 리스트 클릭하면 해당 페이지로 이동
    @Override
    public void showNext(int position) {
        //position 0번은 메인페이지
        viewPager.setCurrentItem(position + 1, true);
    }
}
