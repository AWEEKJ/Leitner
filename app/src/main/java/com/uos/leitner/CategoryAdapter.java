package com.uos.leitner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changhyeon on 2016. 10. 30..
 */

class CategoryAdapter extends ArrayAdapter<Category> {
    private List<Category> items;
    DatabaseHelper db;

    public CategoryAdapter(Context context, int textViewResourceId, List<Category> items) {
        super(context, textViewResourceId, items);
        db = new DatabaseHelper(getContext());

        //items = db.getAllCategories();
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

            if (subject_Level != null)
                subject_Level.setText("현재 레벨: " + c.getCurrentLevel());

            if (subject_MaxTime != null)
                subject_MaxTime.setText("목표 시간: " + c.getMaxTime());
        }

        return view;
    }
}