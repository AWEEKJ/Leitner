package com.uos.leitner;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uos.leitner.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changhyeon on 2016. 10. 30..
 */

class CategoryAdapter extends ArrayAdapter<Category> {

    private ArrayList<Category> items;

    public CategoryAdapter(Context context, int textViewResourceId, ArrayList<Category> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_category, null);
        }

        Category c = items.get(position);

        if (c != null) {
            TextView subject_Name = (TextView) view.findViewById(R.id.Category_Name);
            TextView subject_Level = (TextView) view.findViewById(R.id.Category_Level);
            TextView subject_MaxTime = (TextView) view.findViewById(R.id.Category_MaxTime);

            if (subject_Name != null)
                subject_Name.setText(c.getSubject_Name());

            if (subject_MaxTime != null)
                subject_MaxTime.setText("목표 시간: " + c.getMaxTime());

            if (subject_Level != null)
                subject_Level.setText("현재 레벨: " + c.getCurrentLevel());
        }

        return view;
    }

    void removeItem(ArrayList<Category> categoryList, int index) {
        categoryList.remove(index);
        ((MainActivity)getContext()).runOnUiThread(new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}