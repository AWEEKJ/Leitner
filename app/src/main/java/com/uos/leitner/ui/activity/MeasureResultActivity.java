package com.uos.leitner.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;
import com.uos.leitner.R;
import com.uos.leitner.database.DatabaseHelper;


/**
 * Created by JungJee on 2016. 11. 10..
 */

public class MeasureResultActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_measure_result);

        Intent intent = getIntent();

        int time_to_try = intent.getExtras().getInt("time_to_try"); //도전할 시간
        int time_to_complete = intent.getExtras().getInt("time_to_complete"); //실시한 시간
        int present_level = intent.getExtras().getInt("present_level");
        int next_level = intent.getExtras().getInt("next_level");
        int next_goal = intent.getExtras().getInt("next_goal");
        int mode = intent.getExtras().getInt("mode");

        TextView presentGoal = (TextView)findViewById(R.id.present_Goal);
        TextView presentLevel = (TextView)findViewById(R.id.present_Level);
        TextView nextGoal = (TextView)findViewById(R.id.next_Goal);
        TextView nextLevel = (TextView)findViewById(R.id.next_Level);
        TextView measureResult = (TextView) findViewById(R.id.measureResult);

        Button confirmBtn = (Button) findViewById(R.id.button_StartAgain);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (mode == 1) { // Leitner timer
            measureResult.setText((time_to_complete-time_to_try)/60 + "' " + (time_to_complete-time_to_try)%60 + "''");
            presentGoal.setText("GOAL TIME : " + time_to_complete/60 + "' " + time_to_complete%60 + "''");
            presentLevel.setText("LEVEL : " +present_level);
            nextGoal.setText("GOAL TIME : " + next_goal/60+"' "+next_goal%60+"''");
            nextLevel.setText("LEVEL : " + next_level);

            if(time_to_try==0) { // 성공
                confirmBtn.setText("CONFIRM");
            }
            else {
                confirmBtn.setText("RETRY");
            }
        } else if (mode == 2) { // Pomodoro timer
            measureResult.setText((time_to_complete-time_to_try)/60 + "' " + (time_to_complete-time_to_try)%60 + "''");
            presentGoal.setText("GOAL TIME : " + time_to_complete/60 + "' " + time_to_complete%60 + "''");
            //presentLevel.setText("LEVEL : " +present_level);
            nextGoal.setText("GOAL TIME : " + next_goal/60+"' "+next_goal%60+"''");
            //nextLevel.setText("LEVEL : " + next_level);

            if(time_to_try==0) { // 성공
                confirmBtn.setText("CONFIRM");
            }
            else {
                confirmBtn.setText("RETRY");
            }
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
