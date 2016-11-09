package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by changhyeon on 2016. 11. 6..
 */

public class VerticalActivity extends Fragment {
    private MyPagerAdapter pagerAdapter;
    private fr.castorflex.android.verticalviewpager.VerticalViewPager viewPager;
    private long ID;
    private static final int MAX=10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vertical, viewPager);

        readBundle(getArguments());

        pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewPager = (fr.castorflex.android.verticalviewpager.VerticalViewPager) view.findViewById(R.id.vertical_pager);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(MAX);

        if (((MainActivity) getActivity()).flag == false) {
            pagerAdapter.add(new CategoryView());   // 처음 메인화면 생성
            pagerAdapter.add(new StatisticView_Total());
            ((MainActivity) getActivity()).flag = true;
        }

        else if (((MainActivity) getActivity()).flag == true) {
            pagerAdapter.add(MeasureView.newInstance(ID));
            pagerAdapter.add(StatisticView_Detail.newInstance());
        }

        return view;
    }

    public static VerticalActivity newInstance(long subject_id) {
        VerticalActivity fragment = new VerticalActivity();
        Bundle args = new Bundle();
        args.putLong("ID", subject_id);
        fragment.setArguments(args);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            ID = bundle.getLong("ID");
        }
    }
}