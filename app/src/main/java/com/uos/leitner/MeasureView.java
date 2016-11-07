package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uos.leitner.helper.DatabaseHelper;

import static java.lang.Math.toIntExact;

/**
 * Created by HANJU on 2016. 11. 2..
 */

public class MeasureView extends Fragment {
    private DatabaseHelper db;
    private Long id;
    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());

        View view = inflater.inflate(R.layout.fragment_measure, null);
        readBundle(getArguments());

        category = db.getCategory(id).getSubject_Name();

        TextView TV = (TextView) view.findViewById(R.id.tv);
        TV.setTextSize(25);
        TV.setText(category);

        return view;
    }

    public static MeasureView newInstance(Long ID){
        MeasureView fragment = new MeasureView();
        Bundle args =  new Bundle();
        args.putLong("id", ID);
        fragment.setArguments(args);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            id = bundle.getLong("id");
        }
    }
}
