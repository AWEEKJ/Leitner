package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by changhyeon on 2016. 10. 31..
 */

public class DetailView extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_view, null);

        return view;
    }

    public static DetailView newInstance(){
        DetailView fragment = new DetailView();
        Bundle args =  new Bundle();
        fragment.setArguments(args);

        return fragment;
    }
}