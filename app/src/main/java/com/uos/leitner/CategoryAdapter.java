package com.uos.leitner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uos.leitner.model.Category;

import java.util.ArrayList;

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
            TextView categoryName = (TextView) view.findViewById(R.id.categoryName);
            TextView categoryLevel = (TextView) view.findViewById(R.id.categoryLevel);
            TextView categoryGoalTime = (TextView) view.findViewById(R.id.categoryGoalTime);

            if (categoryName != null)
                categoryName.setText(c.getSubject_Name());

            if (categoryLevel != null)
                categoryLevel.setText(c.getCurrentLevel()+" LV.");

            if (categoryGoalTime != null)
                categoryGoalTime.setText(c.getMaxTime()+" M");

        }

        return view;
    }

    void removeItem(int index) {
        items.remove(index);
        ((MainActivity)getContext()).runOnUiThread(new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}