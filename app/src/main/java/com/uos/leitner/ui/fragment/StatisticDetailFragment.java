package com.uos.leitner.ui.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.uos.leitner.R;
import com.uos.leitner.database.DatabaseHelper;
import com.uos.leitner.model.Subject_log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HANJU on 2016. 11. 7..
 */

public class StatisticDetailFragment extends Fragment {

    private long id;

    // BarChart
    BarChart barChart;
    ArrayList<BarEntry> barEntries;

    // PieChart
    PieChart pieChart;
    ArrayList<PieEntry> pieEntries;

    TextView textView;
    TextView textView2;

    // Database Helper
    DatabaseHelper db;

    public static StatisticDetailFragment newInstance(Long ID) {
        StatisticDetailFragment fragment = new StatisticDetailFragment();
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

        View view = inflater.inflate(R.layout.fragment_statistic_detail, null);
        readBundle(getArguments());

        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        pieChart = (PieChart) view.findViewById(R.id.pie_chart_hit_rate);
        textView = (TextView) view.findViewById(R.id.textView);
        db = new DatabaseHelper(getContext());

        createBarGraph(id);
        createPieChart((int)id);
        textView.setText("누적 시간\n");
        textView.append(String.valueOf(db.getSumTime((int)id)));

        return view;
    }
    public void createBarGraph(long subject_id) {

        barEntries = new ArrayList<>();
        Description dsc = new Description();
        dsc.setText("");

        List<Subject_log> sls = db.getSomeSubject_log(subject_id);
        int index = 1;

        if (!sls.isEmpty()) {
            for (Subject_log sl : sls) {
                // 그래프 그릴 값을 할당
                barEntries.add(new BarEntry(index++, new float[]{sl.getTime_to_try(),
                        Math.abs(sl.getTime_to_try() - sl.getTime_to_complete()) }));
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, ""/*db.getCategory(subject_id).getSubject_Name()*/);
//            barDataSet.setStackLabels(new String[] {"Success", "Fail"});
            barDataSet.setColors(getColors());
            barDataSet.setValueTextSize(12f);
            //barDataSet.setBarBorderWidth(1f);
            barDataSet.setValueFormatter(new MyValueFormatter());

            BarData barData = new BarData(barDataSet);
            barData.setValueTextColor(Color.WHITE);
            barChart.setData(barData);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setDrawGridLines(false);
//            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setTextSize(10f);
            xAxis.setTextColor(Color.WHITE);
//            xAxis.setDrawAxisLine(true);
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setTextColor(Color.WHITE);
            leftAxis.setLabelCount(4);
//            leftAxis.setGridColor(Color.parseColor("#E6D4D4"));
//            leftAxis.setTextSize(14 /*textSize*/);
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
            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setEnabled(false);

            barChart.animateY(3000);
            barChart.setFitBars(true);
            barChart.setDescription(dsc);

            Legend l = barChart.getLegend();
            l.setTextColor(Color.WHITE);
            barChart.invalidate();
        }

    }

    public void createPieChart(int subject_id) {
        pieEntries = new ArrayList<>();
        int successCount = db.getSuccessCount(subject_id);
        int tryCount = db.getTryCount(subject_id);
        float successRate = Float.parseFloat(String.format("%.2f", ((float)successCount / (float)tryCount) * 100));

        Description dsc = new Description();
        dsc.setText("");

        pieEntries.add(new PieEntry(successRate, "SUCCESS"));
        pieEntries.add(new PieEntry(100-successRate, "FAIL"));

        PieDataSet set = new PieDataSet(pieEntries, "");
        set.setColors(getColors());
        set.setValueTextSize(12.0f);
        set.setSliceSpace(3f);
        set.setSelectionShift(5f);
        set.setValueFormatter(new PercentFormatter());
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.animateY(3000);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDescription(dsc);
        Legend l = pieChart.getLegend();
        l.setTextColor(Color.WHITE);

        pieChart.invalidate(); // refresh
    }

    private int[] getColors() {
        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        colors[0] = ContextCompat.getColor(getContext(), R.color.colorDetailStatisticChart1);
        colors[1] = ContextCompat.getColor(getContext(), R.color.colorDetailStatisticChart2);

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

    public class yxixValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return Math.round(value)+" $";

        }
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString(""+db.getCategory(id).getSubject_Name());
        //s.setSpan(new RelativeSizeSpan(2f), 0, 3, 0);
        //s.setSpan(new StyleSpan(Typeface.NORMAL), 3, s.length(), 0);

        return s;
    }


}