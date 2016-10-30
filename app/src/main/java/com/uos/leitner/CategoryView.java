package com.uos.leitner;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by changhyeon on 2016. 10. 30..
 */

public class CategoryView extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.categort_view, null);

        final ArrayList<Category> categoryList = new ArrayList<Category>();
        CategoryAdapter adapter = new CategoryAdapter(this.getActivity(), R.layout.category_list, categoryList) ;
        final ListView listView = (ListView)view.findViewById(R.id.ListView);   //  ListView는 XML ListView
        listView.setAdapter(adapter);

        final Button addButton = (Button)view.findViewById(R.id.addButton);
        final LinearLayout ly = (LinearLayout)view.findViewById(R.id.insertPopup);
        final Button insertButton = (Button)view.findViewById(R.id.insertButton);
        final EditText insertName = (EditText)view.findViewById(R.id.insertName);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        addButton.setOnClickListener(new View.OnClickListener() {   //항목 추가 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                ly.setVisibility(View.VISIBLE);
                insertName.requestFocus();
                imm.showSoftInput(insertName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {    // 추가 확인 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                String name = insertName.getText().toString();
                Category tmp = new Category(name, "0분");

                categoryList.add(tmp);

                ly.setVisibility(View.GONE);
                insertName.setText("");
                insertName.clearFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        return view;
    }
}

