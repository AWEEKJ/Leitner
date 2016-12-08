package com.uos.leitner.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * Created by HANJU on 2016. 12. 7..
 */

public class CategoryEditTab1Fragment extends Fragment {

    private DatabaseHelper db;
    private Category category;
    private EditText categoryNameET;
    private SeekBar goalTimeSB;
    private TextView goalTimeTV;
    private SeekBar levelSB;
    private TextView levelTV;
    private Button saveBTN;
    private Intent intent;
    private int count;

    private int categoryId;

    public static CategoryEditTab1Fragment newInstance(int categoryId) {
        CategoryEditTab1Fragment fragment = new CategoryEditTab1Fragment();
        Bundle args = new Bundle();
        args.putInt("categoryId", categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            categoryId = bundle.getInt("categoryId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_category_add_tab1, null);
        readBundle(getArguments());

        intent = new Intent(getActivity(), MainActivity.class);

        count = getActivity().getIntent().getIntExtra("count", count);

        db = new DatabaseHelper(getActivity());
        category = db.getCategory(Long.valueOf(categoryId));

        categoryNameET = (EditText) view.findViewById(R.id.categoryNameEditText);
        categoryNameET.setText(category.getSubject_Name());

        goalTimeSB = (SeekBar) view.findViewById(R.id.goalTimeSeekBar);
        goalTimeSB.setProgress(category.getMaxTime());
        goalTimeTV = (TextView) view.findViewById(R.id.goalTimeValueTextView);
        goalTimeTV.setText(String.valueOf(goalTimeSB.getProgress()));

        goalTimeSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int min = 1;
                if (progress < min) {
                    goalTimeTV.setText(String.valueOf(1));
                }
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

        levelSB = (SeekBar) view.findViewById(R.id.levelSeekBar);
        levelSB.setProgress(category.getCurrentLevel());
        levelTV = (TextView) view.findViewById(R.id.levelValueTextView);
        levelTV.setText(String.valueOf(levelSB.getProgress()));

        levelSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int min = 1;
                if (progress < min) {
                    levelTV.setText(String.valueOf(1));
                }
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

        saveBTN = (Button) view.findViewById(R.id.saveButton);
        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String categoryName = String.valueOf(categoryNameET.getText());
                int goalTime = goalTimeSB.getProgress();
                int level = levelSB.getProgress();
                int mode = 1; // mode 1 is Leitner Timer

                category.setSubject_Name(categoryName);
                category.setMaxTime(goalTime);
                category.setCurrentLevel(level);
                category.setMode(mode);

                db.updateCategory(category);

                count++;
                intent.putExtra("count", count);
                getActivity().finish();
                startActivity(intent);
            }

        });

        return view;
    }
}
