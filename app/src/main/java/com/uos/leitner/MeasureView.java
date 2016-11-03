package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by HANJU on 2016. 11. 2..
 */

public class MeasureView extends Fragment {

    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measure, null);
        readBundle(getArguments());

        TextView TV = (TextView) view.findViewById(R.id.tv);
        TV.setTextSize(25);
        TV.setText(category);

        return view;
    }


    public static MeasureView newInstance(String name){
        MeasureView fragment = new MeasureView();
        Bundle args =  new Bundle();
        args.putString("category", name);
        fragment.setArguments(args);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            category = bundle.getString("category");
        }
    }
}
