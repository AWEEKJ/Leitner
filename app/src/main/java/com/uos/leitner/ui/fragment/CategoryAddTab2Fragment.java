package com.uos.leitner.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uos.leitner.R;
import com.uos.leitner.database.DatabaseHelper;
import com.uos.leitner.model.Category;
import com.uos.leitner.ui.activity.MainActivity;

/**
 * Created by HANJU on 2016. 12. 2..
 */

public class CategoryAddTab2Fragment extends Fragment {

    private DatabaseHelper db;
    private EditText categoryNameET;
    private SeekBar goalTimeSB;
    private TextView goalTimeTV;
    private Button saveBTN;
    private Intent intent;
    private int count;

    static int MAX = new MainActivity().getMAX();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_category_add_tab2, null);

        intent = new Intent(getActivity(), MainActivity.class);

        count = getActivity().getIntent().getIntExtra("count", count);

        db = new DatabaseHelper(getActivity());

        categoryNameET = (EditText) view.findViewById(R.id.categoryNameEditText);

        goalTimeSB = (SeekBar) view.findViewById(R.id.goalTimeSeekBar);
        goalTimeSB.setProgress(25);     // 초기시간으로 25분
        goalTimeTV = (TextView) view.findViewById(R.id.goalTimeValueTextView);
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

        saveBTN = (Button) view.findViewById(R.id.saveButton);
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String categoryName = String.valueOf(categoryNameET.getText());
                int goalTime = goalTimeSB.getProgress();
                int mode = 2; // mode 2 is Pomodoro Timer

                if(count<MAX) {
                    Category newCategory = new Category(categoryName, 10, goalTime, mode);
                    db.createCategory(newCategory);
                    count++;
                    intent.putExtra("count", count);
                    getActivity().finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "생성할 수 있는 카테고리 개수는 5개입니다.", Toast.LENGTH_LONG).show();
                }
            }

        });

        return view;
    }
}