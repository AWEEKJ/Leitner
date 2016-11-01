package com.uos.leitner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
            view = inflater.inflate(R.layout.category_list, null);
        }

        Category c = items.get(position);

        if (c != null) {
            TextView tvName = (TextView) view.findViewById(R.id.Category_Name);
            TextView tvTime = (TextView) view.findViewById(R.id.Category_Time);

            if (tvName != null)
                tvName.setText(c.getName());

            if (tvTime != null)
                tvTime.setText("집중 시간: " + c.getTime());
        }

        return view;
    }
}