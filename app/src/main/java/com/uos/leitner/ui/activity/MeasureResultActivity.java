package com.uos.leitner.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.uos.leitner.R;


/**
 * Created by JungJee on 2016. 11. 10..
 */

public class MeasureResultActivity extends Activity implements View.OnClickListener{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_measure_result);

        Intent intent = getIntent();

        int time_to_try = intent.getExtras().getInt("time_to_try");
        int time_to_complete = intent.getExtras().getInt("time_to_complete");

//        Button start_again = (Button)findViewById(R.id.button_StartAgain);

        TextView result = (TextView)findViewById(R.id.present_result);

        findViewById(R.id.button_StartAgain).setOnClickListener(this);

//        result.setText("목표한 집중시간  남았습니다."); // ok
//        result.setText("목표한 집중시간 %d분 중 %d 분이 남았습니다.", time_to_complete, time_to_try);
        result.setText("목표한 집중시간" + time_to_complete/60 +  "분 " + time_to_complete%60 + "초 중 "+ time_to_try/60 + "분 " + time_to_try%60 + " 초가 남았습니다.");
    }


    public void onClick(View view) {
        finish();
    }
}
