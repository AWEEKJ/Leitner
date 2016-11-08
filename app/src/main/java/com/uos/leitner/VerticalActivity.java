package com.uos.leitner;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uos.leitner.model.Category;

import java.util.ArrayList;

/**
 * Created by changhyeon on 2016. 11. 6..
 */

public class VerticalActivity extends Fragment {
    private MyPagerAdapter pagerAdapter;
    private fr.castorflex.android.verticalviewpager.VerticalViewPager viewPager;
    private long ID;

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
        viewPager.setOffscreenPageLimit(10);

        if(((MainActivity)getActivity()).flag == false) {
            pagerAdapter.add(new CategoryView());   // 처음 메인화면 생성
            ((MainActivity)getActivity()).flag =true;
        }

         else if(((MainActivity)getActivity()).flag ==true) {
            pagerAdapter.add(MeasureView.newInstance(ID));
            pagerAdapter.add(StatisticView.newInstance());
        }

        int x = pagerAdapter.getCount();
        Log.d("몇개야야야야", Integer.toString(x));

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

    public void del(int position) {
        Log.d("VERTICAL VIEW", Integer.toString(position));
//        PagerAdapter.POSITION_NONE;
//        int index = 2*position+2;
//        pagerAdapter.remove(0);
//        pagerAdapter.remove(1);
//        pagerAdapter.remove(position+2);

//        pagerAdapter.notifyDataSetChanged();
//        if (pagerAdapter.getCount()) {}


    }
}