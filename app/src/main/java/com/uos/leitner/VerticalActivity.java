package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by changhyeon on 2016. 11. 6..
 */

public class VerticalActivity extends Fragment {
    private MyPagerAdapter pagerAdapter;
    private fr.castorflex.android.verticalviewpager.VerticalViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vertical, viewPager);

        pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewPager = (fr.castorflex.android.verticalviewpager.VerticalViewPager) view.findViewById(R.id.vertical_pager);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(10);

        if(((MainActivity)getActivity()).flag == false) {
            pagerAdapter.add(new CategoryView());   // 처음 메인화면 생성
            ((MainActivity)getActivity()).flag =true;
        }

        else if(((MainActivity)getActivity()).flag ==true) {
//            Log.d("test", getArguments().toString() );
            Fragment fragment = MeasureView.newInstance(getArguments().toString());
            pagerAdapter.add(fragment);

        }

        pagerAdapter.add(new StatisticView());

        return view;
    }

    public static VerticalActivity newInstance(String name) {
        VerticalActivity fragment = new VerticalActivity();
        Bundle args = new Bundle();
        args.putString("", name);
        fragment.setArguments(args);

        return fragment;
    }
}