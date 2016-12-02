package com.uos.leitner.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uos.leitner.R;
import com.uos.leitner.ui.adapter.CategoryListPagerAdapter;
import com.uos.leitner.ui.activity.MainActivity;

/**
 * Created by changhyeon on 2016. 11. 6..
 */

public class CategoryListVerticalPagerFragment extends Fragment {
    private CategoryListPagerAdapter pagerAdapter;
    private fr.castorflex.android.verticalviewpager.VerticalViewPager viewPager;
    private long ID;
    private static final int MAX=2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list_vertical, viewPager);

        readBundle(getArguments());

        pagerAdapter = new CategoryListPagerAdapter(getChildFragmentManager());
        viewPager = (fr.castorflex.android.verticalviewpager.VerticalViewPager) view.findViewById(R.id.vertical_pager);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(MAX);

        if (((MainActivity) getActivity()).flag == false) {
            pagerAdapter.add(new CategoryListFragment());   // 처음 메인화면 생성
            pagerAdapter.add(new StatisticTotalFragment());

            ((MainActivity) getActivity()).flag = true;
        }

        else if (((MainActivity) getActivity()).flag == true) {
            pagerAdapter.add(MeasureFragment.newInstance(ID));
            pagerAdapter.add(StatisticDetailFragment.newInstance(ID));
        }

        return view;
    }

    public static CategoryListVerticalPagerFragment newInstance(long subject_id) {
        CategoryListVerticalPagerFragment fragment = new CategoryListVerticalPagerFragment();
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