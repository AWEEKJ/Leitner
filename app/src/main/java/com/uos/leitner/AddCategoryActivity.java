package com.uos.leitner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;

/**
 * Created by HANJU on 2016. 11. 10..
 */

public class AddCategoryActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText categoryNameET;
    private SeekBar goalTimeSB;
    private TextView goalTimeTV;
    private Button saveBTN;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        intent = new Intent(this, MainActivity.class);

        db = new DatabaseHelper(this);

        categoryNameET = (EditText) findViewById(R.id.categoryNameEditText);
        goalTimeSB = (SeekBar) findViewById(R.id.goalTimeSeekBar);
        goalTimeSB.setProgress(25);     // 초기값으로 25분
        goalTimeTV = (TextView) findViewById(R.id.goalTimeValueTextView);
        goalTimeTV.setText(String.valueOf(goalTimeSB.getProgress()));
        saveBTN = (Button) findViewById(R.id.saveButton);

        goalTimeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                goalTimeTV.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                goalTimeTV.setText(goalTimeTV.getText());
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String categoryName = String.valueOf(categoryNameET.getText());
                int level = 5;
                int goalTime = goalTimeSB.getProgress();

                Category newCategory = new Category(categoryName, level, goalTime);
                db.createCategory(newCategory);

                finish();
                startActivity(intent);
            }

        });



    }
}
