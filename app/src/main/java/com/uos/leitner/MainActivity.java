package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;

import java.util.ArrayList;

// 가로로 추가되는 ViewPager 생성
public class MainActivity extends AppCompatActivity implements CategoryView.Communicator {

    protected int MAX = 5;   // 생성가능한 페이지 갯수
    protected boolean flag = false;
    private DatabaseHelper db;

    private MyPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_pager);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(MAX);

        pagerAdapter.add(new VerticalActivity());   // MainActivity의 Adapter는 VerticalViewPager를 항목으로 가짐.
    }

    //뒤로가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) super.onBackPressed();

        else viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    // CategoryView의 인터페이스 구현
    //1. DB 정보를 받아서 초기화
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

    //2. 새로운 카테고리의 세부 페이지 생성
    @Override
    public void addCategory(long id) {
        if (pagerAdapter.getCount() <= 10) {
            Fragment fragment = VerticalActivity.newInstance(id);

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
    public void update(int position) {
//        VerticalActivity a = new VerticalActivity();
//        a.del(position);

        pagerAdapter.remove(position);
        pagerAdapter.notifyDataSetChanged();
    }
}
