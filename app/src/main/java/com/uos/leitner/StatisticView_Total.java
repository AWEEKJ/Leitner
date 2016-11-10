package com.uos.leitner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;
import com.uos.leitner.model.Subject_log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

/**
 * Created by HANJU on 2016. 11. 7..
 */

public class StatisticView_Total extends Fragment {

    // LineChart
    LineChart lineChart;
    List<Entry> lineEntries;

    DatabaseHelper db;

    public static StatisticView_Total newInstance() {
        StatisticView_Total fragment = new StatisticView_Total();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_total_statistic, null);

        lineChart = (LineChart)view.findViewById(R.id.line_chart);

        db = new DatabaseHelper(getContext());

        createLineGraph();
        db.close();

        return view;
    }

    public LineDataSet createSingleLineGraph(long subject_id) {
        int index = 1;
        lineEntries = new ArrayList<Entry>();
        LineDataSet lineDataSet =  null;
        List<Subject_log> sls = db.getSomeSubject_log(subject_id);
        if(!sls.isEmpty()) {
            for (Subject_log sl : sls) {
                lineEntries.add(new Entry(index++, sl.getTime_to_try()));
            }

            lineDataSet = new LineDataSet(lineEntries, db.getCategory(subject_id).getSubject_Name());
            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        }

        return lineDataSet;
    }

    public void createLineGraph() {
        List<Category> cts = db.getAllCategories();
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        LineDataSet lineDataSet;
        int myColor = 1;
        for(Category ct : cts) {
            // 해당 카테고리에 subject_log가 없는 경우 그래프를 생성하지 않는다.
            lineDataSet = createSingleLineGraph(ct.getSubject_ID());
            if(lineDataSet != null) {
                lineDataSet.setColor(ColorTemplate.COLORFUL_COLORS[myColor]);
                lineDataSet.setCircleColor(ColorTemplate.COLORFUL_COLORS[myColor++]);
                lineDataSet.setCircleRadius(10f);
                lineDataSet.setLineWidth(5f);
                lineDataSet.setValueTextSize(12f);
                dataSets.add(lineDataSet);
            }
        }

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.animateX(1000);
        lineChart.invalidate();

    }

}