package com.uos.leitner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HANJU on 2016. 11. 2..
 */

public class TimerView extends Fragment{
    // Time Setup for temporary
    int minute = 13;
    int second = 22;

    // Values displayed by the timer
    private int displayMinutes;
    private int displaySeconds;

    public TimerView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_timer, null);
        //this.displayMinutes = (TextView) view.findViewById(R.id.minutesTV);



        return inflater.inflate(R.layout.fragment_timer, container, false);

    }
}
