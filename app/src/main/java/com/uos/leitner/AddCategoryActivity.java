package com.uos.leitner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private SeekBar levelSB;
    private TextView levelTV;
    private Button saveBTN;
    private Intent intent;
    private MainActivity ma = new MainActivity();
    private int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        intent = new Intent(this, MainActivity.class);

        count = getIntent().getIntExtra("count", count);

        db = new DatabaseHelper(this);

        categoryNameET = (EditText) findViewById(R.id.categoryNameEditText);

        goalTimeSB = (SeekBar) findViewById(R.id.goalTimeSeekBar);
        goalTimeSB.setProgress(25);     // 초기시간으로 25분
        goalTimeTV = (TextView) findViewById(R.id.goalTimeValueTextView);
        goalTimeTV.setText(String.valueOf(goalTimeSB.getProgress()));

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

        levelSB = (SeekBar) findViewById(R.id.levelSeekBar);
        levelSB.setProgress(5);     // 초기레벨로 5단계
        levelTV = (TextView) findViewById(R.id.levelValueTextView);
        levelTV.setText(String.valueOf(levelSB.getProgress()));

        levelSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                levelTV.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                levelTV.setText(levelTV.getText());
            }
        });

        saveBTN = (Button) findViewById(R.id.saveButton);
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String categoryName = String.valueOf(categoryNameET.getText());
                int level = levelSB.getProgress();
                int goalTime = goalTimeSB.getProgress();

                if(count<ma.MAX) {
                    Category newCategory = new Category(categoryName, level, goalTime);
                    db.createCategory(newCategory);
                    count++;

                }

                else {
                    Toast.makeText(AddCategoryActivity.this, "생성 가능한 개수 초과", Toast.LENGTH_LONG).show();
                }

                intent.putExtra("count", count);
                finish();
                startActivity(intent);
            }

        });
    }
}
