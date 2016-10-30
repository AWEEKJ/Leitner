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
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.category_list, null);
        }

        Category c = items.get(position);

        if (c != null) {
            TextView tt = (TextView) v.findViewById(R.id.Category_Name);
            TextView bt = (TextView) v.findViewById(R.id.Category_Time);
            if (tt != null)
                tt.setText(c.getName());
            if (bt != null)
                bt.setText("집중 시간: " + c.getTime());
        }

        return v;
    }
}