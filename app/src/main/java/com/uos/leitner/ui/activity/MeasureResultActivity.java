package com.uos.leitner.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.uos.leitner.R;
import com.uos.leitner.database.DatabaseHelper;


/**
 * Created by JungJee on 2016. 11. 10..
 */

public class MeasureResultActivity extends Activity implements View.OnClickListener{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_measure_result);

        Intent intent = getIntent();

        int time_to_try = intent.getExtras().getInt("time_to_try"); //도전할 시간
        int time_to_complete = intent.getExtras().getInt("time_to_complete"); //실시한 시간
        int present_level = intent.getExtras().getInt("present_level");
        int next_goal = intent.getExtras().getInt("next_goal");

        TextView presentGoal = (TextView)findViewById(R.id.present_Goal);
        TextView presentState = (TextView)findViewById(R.id.present_State);
        TextView nextGoal = (TextView)findViewById(R.id.next_Goal);
        TextView message = (TextView)findViewById(R.id.message);


        findViewById(R.id.button_StartAgain).setOnClickListener(this);

        presentGoal.setText("현재 목표시간 : " + time_to_complete/60 +  "분 " + time_to_complete%60 + "초");
        presentState.setText(present_level+"단계 결과 : " + (time_to_complete-time_to_try)/60 + "분 " + (time_to_complete-time_to_try)%60 + "초");
        nextGoal.setText("다음 목표시간 : " + next_goal/60+"분 "+next_goal%60+"초");

        if(time_to_try==0) { // 성공
            message.setText("축하합니다!");
            String strColor = "#0066CC";
            message.setTextColor(Color.parseColor(strColor));
        }
        else {
            message.setText("다시 도전해보세요!");
            String strColor = "#CC0066";
            message.setTextColor(Color.parseColor(strColor));
        }
    }

    public void onClick(View view) {
        finish();
    }
}
