package com.uos.leitner.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.uos.leitner.R;
import com.uos.leitner.database.DatabaseHelper;
import com.uos.leitner.model.Category;
import com.uos.leitner.model.CategoryCount;
import com.uos.leitner.model.Subject_log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HANJU on 2016. 11. 7..
 */

public class StatisticTotalFragment extends Fragment {

    // LineChart
    LineChart lineChart;
    List<Entry> lineEntries;

    // PieChart
    PieChart pieChart;
    ArrayList<PieEntry> pieEntries;

    DatabaseHelper db;

    public static StatisticTotalFragment newInstance() {
        StatisticTotalFragment fragment = new StatisticTotalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistic_total, null);

        lineChart = (LineChart)view.findViewById(R.id.line_chart);
        pieChart = (PieChart)view.findViewById(R.id.pie_chart);

        db = new DatabaseHelper(getContext());

        createPieChart();
        createLineGraph();

        db.close();

        return view;
    }

    // categorycount를 받아와서 그림을 그려야 한다
    public void createPieChart() {
        pieEntries = new ArrayList<>();
        ArrayList<CategoryCount> cc = db.getAllCategoryCount();
        int totalLogCount = db.totalLogCount();
        float tempRate = 0;

        Description dsc = new Description();
        dsc.setText("");

        if(!cc.isEmpty()) {
            for(CategoryCount c : cc) {
                tempRate = Float.parseFloat(String.format("%.2f",
                        ((float)c.getSubject_count() / (float)totalLogCount) * 100));
                pieEntries.add(new PieEntry(tempRate, c.getSubject_Name()));
            }
        }

        PieDataSet set = new PieDataSet(pieEntries, "");
        set.setValueTextSize(12.0f);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setSliceSpace(3f);
        set.setSelectionShift(5f);
        set.setValueFormatter(new PercentFormatter());

        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.setDescription(dsc);
        pieChart.animateY(3000);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.invalidate(); // refresh
    }


    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("A+");
        s.setSpan(new RelativeSizeSpan(2f), 0, 2, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 2, s.length(), 0);

        return s;
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
        int myColor = 0;

        Description dsc = new Description();
        dsc.setText("");

        for(Category ct : cts) {
            // 해당 카테고리에 subject_log가 없는 경우 그래프를 생성하지 않는다.
            lineDataSet = createSingleLineGraph(ct.getSubject_ID());
            if(lineDataSet != null) {
                lineDataSet.setColor(ColorTemplate.COLORFUL_COLORS[myColor]);
                lineDataSet.setCircleColor(ColorTemplate.COLORFUL_COLORS[myColor++]);
                lineDataSet.setCircleRadius(6f);
                lineDataSet.setLineWidth(3f);
                lineDataSet.setValueTextSize(12f);
                dataSets.add(lineDataSet);
            }
        }

        LineData data = new LineData(dataSets);
        lineChart.setData(data);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
//            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setTextSize(10f);
//            xAxis.setTextColor(Color.RED);
//            xAxis.setDrawAxisLine(true);
        YAxis leftAxis = lineChart.getAxisLeft();
//            leftAxis.setGridColor(Color.parseColor("#E6D4D4"));
//            leftAxis.setTextSize(14 /*textSize*/);
        leftAxis.setLabelCount(4);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int s = (int) value/60;

                String k = "";
                k = s + "분";

                return k;
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        leftAxis.setDrawLimitLinesBehindData(true);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        lineChart.animateX(1000);
        lineChart.setDescription(dsc);
        lineChart.invalidate();

    }

}