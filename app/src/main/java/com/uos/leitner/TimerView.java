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

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by HANJU on 2016. 11. 2..
 */

public class TimerView extends Fragment {

    private long goalTime = 6000; // Time Setup for temporary, 13m 22s is 802000 milliseconds
    private long remainTime;

    private static final String FORMAT = "%02d";
    private CountDownTimer cTimer;
    private TextView minutesTV;
    private TextView secondsTV;
    private Button startBtn;
    private Button stopBtn;
    private DonutProgress progressBar;

    private TextView TV; // 남은 시간 테스트

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
        progressBar = (DonutProgress) view.findViewById(R.id.progressBar);

        TV = (TextView) view.findViewById(R.id.test); // 남은 시간 테스트

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        remainTime = goalTime;

        minutesTV.setText(""+String.format(Locale.US, FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(goalTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(goalTime))));

        secondsTV.setText(""+String.format(Locale.US, FORMAT,
                TimeUnit.MILLISECONDS.toSeconds(goalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(goalTime))));

        progressBar.setProgress(0);

        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cTimer = new CountDownTimer(goalTime, 100) {

                    public void onTick(long millisUntilFinished) {
                        if (Math.round((float)millisUntilFinished / 1000.0f) != remainTime) {
                            remainTime = Math.round((float)millisUntilFinished / 1000.0f);

                            progressBar.setProgress((int)((goalTime-remainTime*1000)*100/goalTime));

                            minutesTV.setText(""+String.format(Locale.US, FORMAT,
                                    TimeUnit.SECONDS.toMinutes(remainTime) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(remainTime))));

                            secondsTV.setText(""+String.format(Locale.US, FORMAT,
                                    TimeUnit.SECONDS.toSeconds(remainTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(remainTime))));

                        }
                    }

                    public void onFinish() {
                        progressBar.setProgress(100);
                    }

                }.start();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cTimer.cancel();
                // TV.setText("seconds remaining: " + remainTime); // 남은 시간 테스트
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
