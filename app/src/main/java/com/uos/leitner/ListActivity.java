package com.uos.leitner;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.ViewGroup.LayoutParams;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;
import android.widget.EditText;
import android.widget.TextView;

public class ListActivity extends Activity {
    ViewFlipper flipper;
    Button nextButton, editButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flipper_test);

        flipper = (ViewFlipper)findViewById(R.id.List);
        nextButton = (Button)findViewById(R.id.button_next);
        editButton = (Button)findViewById(R.id.editButton);
        editText = (EditText) findViewById(R.id.editText);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showNext();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = editText.getText().toString();
                TextView tv = new TextView(getApplicationContext());

                if(text.length()==0) return;

                tv.setText(text);
                tv.setLayoutParams(new DrawerLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,

                        ViewGroup.LayoutParams.WRAP_CONTENT));

                tv.setGravity(Gravity.CENTER_HORIZONTAL);

                flipper.addView(tv);
            }
        });
    }
}
