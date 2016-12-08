package com.uos.leitner.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.uos.leitner.database.DatabaseHelper;
import com.uos.leitner.ui.activity.MeasureResultActivity;
import com.uos.leitner.model.Category;
import com.uos.leitner.model.Subject_log;
import com.uos.leitner.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by HANJU on 2016. 11. 2..
 */

public class MeasureFragment extends Fragment {
    private DatabaseHelper db;

    private Long categoryId;
    private Category category;
    private String categoryName;

    private long goalTime; // Time Setup for temporary, 13m 22s is 802000 milliseconds
    private long remainTime;

    private int currentLevel;
    private double maxTime;

    private static final String FORMAT = "%02d";
    private CountDownTimer cTimer;
    private boolean isTimerRunning = false;

    private TextView categoryNameTV;
    private TextView minutesTV;
    private TextView secondsTV;
    private Button startBtn;
    CircularProgressBar circularProgressBar;

    public static MeasureFragment newInstance(Long ID) {
        MeasureFragment fragment = new MeasureFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_measure, null);
        readBundle(getArguments());

        category = db.getCategory(categoryId);
        categoryName = category.getSubject_Name();
        categoryNameTV = (TextView) view.findViewById(R.id.categoryNameTextView);
        categoryNameTV.setTextSize(25);
        categoryNameTV.setText(categoryName);
        currentLevel = category.getCurrentLevel();
        maxTime = category.getMaxTime()*60000;

        int timerMode = category.getMode();

        if (timerMode == 1) {
            goalTime = db.getTryTime(currentLevel, maxTime);
        } else if (timerMode == 2) {
            goalTime = (long) maxTime;
        }

        minutesTV = (TextView) view.findViewById(R.id.minutesTextView);
        secondsTV = (TextView) view.findViewById(R.id.secondsTextView);
        startBtn = (Button) view.findViewById(R.id.startButton);
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);

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

        circularProgressBar.setProgressWithAnimation(0, 100);

        startBtn.setText(R.string.fa_icon_play);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTimerRunning) {
                    runTimer();
                } else {
                    stopTimer();
                }

            }
        });
    }

    private void runTimer() {
        startBtn.setText(R.string.fa_icon_pause);
        cTimer = new CountDownTimer(goalTime, 100) {

            public void onTick(long millisUntilFinished) {
                isTimerRunning = true;
                if (Math.round((float) millisUntilFinished / 1000.0f) != remainTime) {
                    remainTime = Math.round((float) millisUntilFinished / 1000.0f);

                    int progressPercent = (int) ((goalTime - remainTime * 1000) * 100 / goalTime);
                    circularProgressBar.setProgressWithAnimation(progressPercent, 100);

                    minutesTV.setText("" + String.format(Locale.US, FORMAT,
                            TimeUnit.SECONDS.toMinutes(remainTime) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(remainTime))));

                    secondsTV.setText("" + String.format(Locale.US, FORMAT,
                            TimeUnit.SECONDS.toSeconds(remainTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(remainTime))));
                }
            }

            public void onFinish() {
                isTimerRunning = false;
                stopTimer();
                Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(500);

            }
        }.start();
    }

    private void stopTimer() {
        isTimerRunning = false;
        cTimer.cancel();

        int time_to_try = (int) (goalTime / 1000 - remainTime); // seconds
        int time_to_complete = (int) (goalTime / 1000); // seconds
        int time_remaining = (int) remainTime;
        int pass_or_fail = 1;
        if (remainTime > 0) pass_or_fail = 0;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int subject_id = categoryId.intValue();

        Subject_log log = new Subject_log(time_to_try, time_to_complete, pass_or_fail, date, subject_id);
        db.createSubjectLog(log);

        int level = category.getCurrentLevel();
        if (pass_or_fail == 1) {
            if (level != 20) {
                category.setCurrentLevel(level + 1);
                db.updateCategory(category);
            }
        } else if (pass_or_fail == 0) {
            if (level != 1) {
                category.setCurrentLevel(level - 1);
                db.updateCategory(category);
            }
        }

        startBtn.setText(R.string.fa_icon_play);
        int next_goal;

        Intent intent = new Intent(this.getActivity(), MeasureResultActivity.class);
        intent.putExtra("time_to_complete", time_to_complete);
        intent.putExtra("time_to_try", time_remaining);
        intent.putExtra("present_level", level);
        intent.putExtra("next_level", category.getCurrentLevel());
        intent.putExtra("mode", category.getMode());

        if(time_remaining==0) { //성공
            next_goal = (int)(this.db.getTryTime(currentLevel+1, maxTime/1000));        }
        else {
            if(currentLevel==1)
                next_goal = (int)(this.db.getTryTime(currentLevel, maxTime/1000));
            else
                next_goal = (int)(this.db.getTryTime(currentLevel-1, maxTime/1000));
        }
        intent.putExtra("next_goal", next_goal);
        startActivity(intent);
        getActivity().recreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}