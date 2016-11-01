package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CategoryView.Communicator {
    protected int MAX = 5;   // 생성가능한 페이지 갯수
    private MyPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.main_pager);

        viewPager.setAdapter(pagerAdapter);

        viewPager.setOffscreenPageLimit(MAX);
        pagerAdapter.add(new CategoryView());   // 처음 메인화면 생성
    }


    //뒤로가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) super.onBackPressed();

        else viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    /* CategoryView의 인터페이스를 구현해서 정보교환*/

    //1. 세부항목별 페이지 생성
    @Override
    public void addCategory() {
        if(pagerAdapter.getCount() <= MAX)
            pagerAdapter.add(new DetailView());

    }

    //2. 카테고리 생성 시, 페이지에 이름 설정
    @Override
    public void setCategoryInfo(String name, int position) {

        Fragment fragment = pagerAdapter.getItem(position); // 새로 생성된 프레그먼트에 대한 참조를 가져오는 곳. 중요.
        TextView TV = (TextView)fragment.getView().findViewById(R.id.tv);
        TV.setTextSize(25);
        TV.setText(name+" 측정 페이지입니다");
    }

    //3. 리스트 클릭하면 해당 페이지로 이동
    @Override
    public void showNext(int position) {
        //position 0번은 메인페이지
        viewPager.setCurrentItem(position+1, true);
    }
}
