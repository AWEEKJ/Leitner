package com.uos.leitner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by JungJee on 2016. 11. 10..
 */

public class PopupResultActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.result_popup);

        Intent intent = getIntent();

        int time_to_try = intent.getExtras().getInt("time_to_try");
        int time_to_complete = intent.getExtras().getInt("time_to_complete");

        TextView result = (TextView)findViewById(R.id.present_result);

//        result.setText("목표한 집중시간  남았습니다."); // ok
//        result.setText("목표한 집중시간 %d분 중 %d 분이 남았습니다.", time_to_complete, time_to_try);
        result.setText("목표한 집중시간" + time_to_complete +  "d분 중 "+ time_to_try + " 분이 남았습니다.");
    }
}
