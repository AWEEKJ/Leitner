package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by changhyeon on 2016. 11. 6..
 */

public class StatisticView extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistic, null);

        // 여기서 통계회면을 제어

        return view;
    }

    public static StatisticView newInstance() {
        StatisticView fragment = new StatisticView();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }
}