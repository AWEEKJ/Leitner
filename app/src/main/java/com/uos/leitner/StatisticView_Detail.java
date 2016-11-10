package com.uos.leitner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Subject_log;

import java.text.DecimalFormat;
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

public class StatisticView_Detail extends Fragment {

    private long id;

    // BarChart
    BarChart barChart;
    ArrayList<BarEntry> barEntries;

    // Database Helper
    DatabaseHelper db;

    public static StatisticView_Detail newInstance(Long ID) {
        StatisticView_Detail fragment = new StatisticView_Detail();
        Bundle args = new Bundle();
        args.putLong("id", ID);
        fragment.setArguments(args);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if(bundle != null) {
            id = bundle.getLong("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistic, null);
        readBundle(getArguments());

        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        db = new DatabaseHelper(getContext());

        createBarGraph(id);

        return view;
    }
    public void createBarGraph(long subject_id) {

        barEntries = new ArrayList<>();
        List<Subject_log> sls = db.getSomeSubject_log(subject_id);
        int index = 1;

        if (!sls.isEmpty()) {
            for (Subject_log sl : sls) {
                // 그래프 그릴 값을 할당
                barEntries.add(new BarEntry(index++, new float[]{sl.getTime_to_try(),
                        Math.abs(sl.getTime_to_try() - sl.getTime_to_complete()) }));
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, db.getCategory(subject_id).getSubject_Name());
            barDataSet.setStackLabels(new String[] {"Time To Complete", "Time To Try"});
            barDataSet.setColors(getColors());
            barDataSet.setValueTextSize(12f);
            barDataSet.setValueFormatter(new MyValueFormatter());

            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.animateY(3000);
            barChart.invalidate();
        }

    }

    private int[] getColors() {
        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];


        colors[0] = Color.rgb(58,172,250);
        colors[1] = Color.rgb(250,88,00);


        return colors;
    }

    public class MyValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if(value > 0)
                return mFormat.format(value);
            else
                return "";
        }
    }

}