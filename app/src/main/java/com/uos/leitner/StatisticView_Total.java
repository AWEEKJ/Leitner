package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by HANJU on 2016. 11. 7..
 */

public class StatisticView_Total extends Fragment {

    BarChart barChart;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;

    public static StatisticView_Total newInstance() {
        StatisticView_Total fragment = new StatisticView_Total();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_total_statistic, null);

        barChart = (BarChart) view.findViewById(R.id.bar_chart2);

        // 이 둘 날짜 사이의 값을 그래프로 보기 위함
        createRandomBarGraph("2016/06/01", "2016/07/31");

        // 여기서 통계회면을 제어

        return view;
    }

    public void createRandomBarGraph(String Date1, String Date2) {
        SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date1 = simpleDataFormat.parse(Date1);
            Date date2 = simpleDataFormat.parse(Date2);

            Calendar mData1 = Calendar.getInstance();
            Calendar mData2 = Calendar.getInstance();
            mData1.clear();
            mData2.clear();

            mData1.setTime(date1);
            mData2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mData1, mData2);

            barEntries = new ArrayList<>();
            float max = 0f;
            float value = 0f;
            Random random = new Random();
            for(int j = 0; j < dates.size(); j++) {
                max = 100f;
                value = random.nextFloat() * max;
                barEntries.add(new BarEntry(value, j));
            }

        } catch(ParseException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        //BarData barData = new BarData(dates, barDataSet);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        //barChart.setDescription("My First Bar Graph");
    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate) {
        ArrayList<String> list = new ArrayList<>();
        while(startDate.compareTo(endDate) <= 0) {
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }

        return list;
    }

    public String getDate(Calendar cld) {
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
                + cld.get(Calendar.DAY_OF_MONTH);
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return curDate;
    }
}