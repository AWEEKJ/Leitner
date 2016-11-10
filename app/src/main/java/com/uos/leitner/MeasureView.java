package com.uos.leitner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
<<<<<<< HEAD

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
=======
>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Subject_log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
<<<<<<< HEAD
import static java.lang.Math.toIntExact;

=======
>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14

/**
 * Created by HANJU on 2016. 11. 2..
 */

public class MeasureView extends Fragment {
    private DatabaseHelper db;

    private Long categoryId;
    private String categoryName;

    private long goalTime; // Time Setup for temporary, 13m 22s is 802000 milliseconds
    private long remainTime;

    private static final String FORMAT = "%02d";
    private CountDownTimer cTimer;
    private boolean isTimerRunning = false;

    private TextView categoryNameTV;
    private TextView minutesTV;
    private TextView secondsTV;
    private Button startBtn;
    private Button stopBtn;
    private DonutProgress progressBar;

    public static MeasureView newInstance(Long ID) {
        MeasureView fragment = new MeasureView();
        Bundle args = new Bundle();
        args.putLong("id", ID);
        fragment.setArguments(args);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            categoryId = bundle.getLong("id");
        }
    }


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

        categoryName = db.getCategory(categoryId).getSubject_Name();
        categoryNameTV = (TextView) view.findViewById(R.id.categoryNameTextView);
        categoryNameTV.setTextSize(25);
        categoryNameTV.setText(categoryName);

<<<<<<< HEAD

        goalTime = db.getCategory(id).getMaxTime()*61000;
=======
        goalTime = db.getCategory(categoryId).getMaxTime()*60000;
>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14

        minutesTV = (TextView) view.findViewById(R.id.minutesTextView);
        secondsTV = (TextView) view.findViewById(R.id.secondsTextView);
        startBtn = (Button) view.findViewById(R.id.startButton);
        stopBtn = (Button) view.findViewById(R.id.stopButton);
        progressBar = (DonutProgress) view.findViewById(R.id.progressBar);

<<<<<<< HEAD
        TV = (TextView) view.findViewById(R.id.test); // 남은 시간 테스트
=======
        //Log.d("TimeCheck", Long.toString(goalTime));
>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14

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
                runTimer();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
            }
        });
    }

    BroadcastReceiver broadcastReceiverStart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runTimer();
        }
    };

    BroadcastReceiver broadcastReceiverStop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopTimer();
        }
    };

<<<<<<< HEAD
                            secondsTV.setText(""+String.format(Locale.US, FORMAT,
                                    TimeUnit.SECONDS.toSeconds(remainTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(remainTime))));
=======
    private void runTimer() {
        if (!isTimerRunning){
            cTimer = new CountDownTimer(goalTime, 100) {
>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14

                public void onTick(long millisUntilFinished) {
                    isTimerRunning = true;
                    if (Math.round((float) millisUntilFinished / 1000.0f) != remainTime) {
                        remainTime = Math.round((float) millisUntilFinished / 1000.0f);

                        progressBar.setProgress((int) ((goalTime - remainTime * 1000) * 100 / goalTime));

                        minutesTV.setText("" + String.format(Locale.US, FORMAT,
                                TimeUnit.SECONDS.toMinutes(remainTime) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(remainTime))));

                        secondsTV.setText("" + String.format(Locale.US, FORMAT,
                                TimeUnit.SECONDS.toSeconds(remainTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(remainTime))));
                    }
<<<<<<< HEAD
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
=======
                }
                public void onFinish() {
                    isTimerRunning = false;
                    progressBar.setProgress(100);
                }
            }.start();
        }
    }

    private void stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            cTimer.cancel();
            //Log.d("STOPED!!!!", "goalTime is "+goalTime+" remainTime is "+remainTime);

            int time_to_try = (int) (goalTime / 1000 - remainTime); // seconds
            int time_to_complete = (int) (goalTime / 1000); // seconds
            int pass_or_fail = 1;
            if (remainTime > 0) pass_or_fail = 0;
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            int subject_id = categoryId.intValue();

            Subject_log log = new Subject_log(time_to_try, time_to_complete, pass_or_fail, date, subject_id);
            db.createSubjectLog(log);



            Intent intent = new Intent(this.getActivity(), PopupResultActivity.class);

            intent.putExtra("time_to_complete", time_to_complete);
            intent.putExtra("time_to_try", time_to_try);

            startActivity(intent);


        }




>>>>>>> 3c8760336ea2b3f3df11b66f929930fb5bfc4d14

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