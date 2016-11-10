package com.uos.leitner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Subject_log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by HANJU on 2016. 11. 7..
 */

public class StatisticView extends Fragment {

    // Bar chart
    BarChart barChart;
    ArrayList<BarEntry> barEntries;

    DatabaseHelper db;
    private Long id;

    public static StatisticView newInstance() {
        StatisticView fragment = new StatisticView();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistic, null);

        barChart = (BarChart) view.findViewById(R.id.bar_chart);

        db = new DatabaseHelper(getContext());

        /* 11월 9일
        * 그래프를 그리기 위해서는 특정 카테고리의 아이디 값을 전달해야하는데 MeasureView 에서 처럼
        * id 값을 받을 수 있도록 연동 해야한다.
        * */
        //createBarGraph(id);

        return view;
    }

    public void createBarGraph(Long subject_id) {

        barEntries = new ArrayList<>();

        List<Subject_log> sls = db.getSomeSubject_log(subject_id);

        for(Subject_log sl : sls) {
            // 그래프 그릴 값을 할당
            barEntries.add(new BarEntry(sl.getLog_no(), new float[] {sl.getTime_to_complete(),
                    sl.getTime_to_try()-sl.getTime_to_complete()}));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Time to Try");
        barDataSet.setColors(getColors());
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
    }

    private int[] getColors() {
        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        /*
        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.COLORFUL_COLORS[i];
        }
        */
        colors[0] = Color.GREEN;
        colors[1] = Color.RED;


        return colors;
    }

}