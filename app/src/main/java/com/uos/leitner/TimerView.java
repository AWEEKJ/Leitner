package com.uos.leitner;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by HANJU on 2016. 11. 2..
 */

public class TimerView extends Fragment{

    private long goalTime = 802000; // Time Setup for temporary, 13m 22s to milliseconds

    private static final String FORMAT = "%02d";
    private CountDownTimer cTimer;

    private TextView minutesTV;
    private TextView secondsTV;
    private Button startBtn;
    private Button stopBtn;

    public TimerView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_timer, null);
        minutesTV = (TextView) view.findViewById(R.id.minutesTextView);
        secondsTV = (TextView) view.findViewById(R.id.secondsTextView);
        startBtn = (Button) view.findViewById(R.id.startButton);
        stopBtn = (Button) view.findViewById(R.id.stopButton);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO 경고 메세지 안 뜨도록 수정할 것
        minutesTV.setText(""+String.format(FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(goalTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(goalTime))));

        secondsTV.setText(""+String.format(FORMAT,
                TimeUnit.MILLISECONDS.toSeconds(goalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(goalTime))));

        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cTimer = new CountDownTimer(goalTime, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {
                        minutesTV.setText(""+String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))));

                        secondsTV.setText(""+String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                        goalTime = millisUntilFinished;
                    }

                    public void onFinish() {

                    }
                }.start();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cTimer.cancel();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
