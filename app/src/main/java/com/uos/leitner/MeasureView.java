package com.uos.leitner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.uos.leitner.helper.DatabaseHelper;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by HANJU on 2016. 11. 2..
 */

public class MeasureView extends Fragment {
    private DatabaseHelper db;
    private Long id;
    private String category;

    private long goalTime; // Time Setup for temporary, 13m 22s is 802000 milliseconds
    private long remainTime;

    private static final String FORMAT = "%02d";
    private CountDownTimer cTimer;
    private TextView categoryNameTV;
    private TextView minutesTV;
    private TextView secondsTV;
    private Button startBtn;
    private Button stopBtn;
    private DonutProgress progressBar;

    private TextView TV; // 남은 시간 테스트

    public static MeasureView newInstance(Long ID) {
        MeasureView fragment = new MeasureView();
        Bundle args = new Bundle();
        args.putLong("id", ID);
        fragment.setArguments(args);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            id = bundle.getLong("id");
        }
    }

    BroadcastReceiver broadcastReceiverStart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // here you can update your db with new messages and update the ui (chat message list)
//            Log.d("Measure Timer", ""+intent);
//            Log.d("Measure Timer", "start Timer!!!");
            cTimer = new CountDownTimer(goalTime, 100) {

                public void onTick(long millisUntilFinished) {
                    if (Math.round((float) millisUntilFinished / 1000.0f) != remainTime) {
                        remainTime = Math.round((float) millisUntilFinished / 1000.0f);

                        progressBar.setProgress((int) ((goalTime - remainTime * 1000) * 100 / goalTime));

                        minutesTV.setText("" + String.format(Locale.US, FORMAT,
                                TimeUnit.SECONDS.toMinutes(remainTime) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(remainTime))));

                        secondsTV.setText("" + String.format(Locale.US, FORMAT,
                                TimeUnit.SECONDS.toSeconds(remainTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(remainTime))));

                    }
                }

                public void onFinish() {
                    progressBar.setProgress(100);
                }

            }.start();

        }
    };

    BroadcastReceiver broadcastReceiverStop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // here you can update your db with new messages and update the ui (chat message list)
//            Log.d("Measure Timer", ""+intent);
//            Log.d("Measure Timer", "start Timer!!!");
            cTimer.cancel();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().startService(new Intent(getActivity(), MotionControlService.class));
        getActivity().registerReceiver(this.broadcastReceiverStart, new IntentFilter("startTimer"));
        getActivity().registerReceiver(this.broadcastReceiverStop, new IntentFilter("stopTimer"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());

        View view = inflater.inflate(R.layout.fragment_measure, null);
        readBundle(getArguments());

        category = db.getCategory(id).getSubject_Name();
        categoryNameTV = (TextView) view.findViewById(R.id.categoryNameTextView);
        categoryNameTV.setTextSize(25);
        categoryNameTV.setText(category);

        goalTime = db.getCategory(id).getMaxTime();

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

        minutesTV.setText("" + String.format(Locale.US, FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(goalTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(goalTime))));

        secondsTV.setText("" + String.format(Locale.US, FORMAT,
                TimeUnit.MILLISECONDS.toSeconds(goalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(goalTime))));

        progressBar.setProgress(0);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cTimer = new CountDownTimer(goalTime, 100) {

                    public void onTick(long millisUntilFinished) {
                        if (Math.round((float) millisUntilFinished / 1000.0f) != remainTime) {
                            remainTime = Math.round((float) millisUntilFinished / 1000.0f);

                            progressBar.setProgress((int) ((goalTime - remainTime * 1000) * 100 / goalTime));

                            minutesTV.setText("" + String.format(Locale.US, FORMAT,
                                    TimeUnit.SECONDS.toMinutes(remainTime) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(remainTime))));

                            secondsTV.setText("" + String.format(Locale.US, FORMAT,
                                    TimeUnit.SECONDS.toSeconds(remainTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(remainTime))));

                        }
                    }

                    public void onFinish() {
                        progressBar.setProgress(100);
                    }

                }.start();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(), MotionControlService.class));
        getActivity().unregisterReceiver(broadcastReceiverStart);
        getActivity().unregisterReceiver(broadcastReceiverStop);
    }
}
