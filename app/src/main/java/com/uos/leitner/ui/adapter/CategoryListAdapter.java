package com.uos.leitner.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uos.leitner.R;
import com.uos.leitner.model.Category;
import com.uos.leitner.ui.activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by changhyeon on 2016. 10. 30..
 */

public class CategoryListAdapter extends ArrayAdapter<Category> {

    private ArrayList<Category> items;

    public CategoryListAdapter(Context context, int textViewResourceId, ArrayList<Category> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_category_list, null);
        }

        Category c = items.get(position);

        if (c != null) {
            TextView categoryName = (TextView) view.findViewById(R.id.categoryName);
            TextView categoryMode = (TextView) view.findViewById(R.id.categoryMode);
            TextView categoryLevel = (TextView) view.findViewById(R.id.categoryLevel);
            TextView categoryGoalTime = (TextView) view.findViewById(R.id.categoryGoalTime);

            int mode = c.getMode();
            categoryName.setText(c.getSubject_Name());

            if (mode == 1) {
                categoryMode.setText("LEITNER");
                categoryLevel.setText("LEVEL : "+c.getCurrentLevel()+"");
            } else if (mode == 2) {
                categoryMode.setText("POMODORO");
            }
            categoryGoalTime.setText("GOAL TIME : "+c.getMaxTime()+"M");
        }

        return view;
    }

    public void removeItem(int index) {
        items.remove(index);
        ((MainActivity)getContext()).runOnUiThread(new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}