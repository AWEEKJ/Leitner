package com.uos.leitner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by changhyeon on 2016. 10. 30..
 */

public class CategoryView extends Fragment {
    private int count=1;
    Communicator hermes = null;
    DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // getApplicationContext() vs getContext()
        db = new DatabaseHelper(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_category, null);

        // final ArrayList<Category> categoryList = new ArrayList<Category>();
        final List<Category> cl = new ArrayList<Category>();
        final CategoryAdapter adapter = new CategoryAdapter(this.getActivity(), R.layout.listview_category, cl);
        final ListView listView = (ListView)view.findViewById(R.id.ListView);   //  ListView는 XML ListView
        listView.setAdapter(adapter);

        final Button addButton = (Button)view.findViewById(R.id.addButton);
        final LinearLayout ly = (LinearLayout)view.findViewById(R.id.insertPopup);
        final Button insertButton = (Button)view.findViewById(R.id.insertButton);
        final EditText insertName = (EditText)view.findViewById(R.id.insertName);
        final EditText maxTime = (EditText)view.findViewById(R.id.maxTime);
        final EditText level = (EditText)view.findViewById(R.id.level);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        // 추가버튼 클릭 이벤트
        addButton.setOnClickListener(new View.OnClickListener() {   //항목 추가 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                ly.setVisibility(View.VISIBLE);
                insertName.requestFocus();
                imm.showSoftInput(insertName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // 추가 확인버튼 클릭 이벤트
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                if(count <= ((MainActivity)getActivity()).MAX) {

                    String name = insertName.getText().toString();
                    int goalTime = Integer.parseInt(maxTime.getText().toString());
                    int currentLevel = Integer.parseInt(level.getText().toString());

                    Category ct = new Category(name, goalTime, currentLevel);
                    db.createCategory(ct);



                    //Category contents = new Category(name, "0분");
                    //categoryList.add(contents);

                    insertName.setText("");
                    maxTime.setText("");
                    level.setText("");
                    insertName.clearFocus();



                    //((MainActivity) getActivity()).addCategory();   // 세부항목 페이지 추가
                    hermes.addCategory(name);
                    count++;
                }
                else
                    Toast.makeText(getContext(), "추가 가능한 개수 초과", Toast.LENGTH_LONG).show();
            }
        });

        // 리스트 항목 클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                hermes.showNext(position);
                Toast.makeText(getContext(), "페이저로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public static CategoryView newInstance(){
        CategoryView fragment = new CategoryView();
        Bundle args =  new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    /* MainActivity에 정보를 전달하기 위한 인터페이스. Hermes로 호출 */
    public interface Communicator {
        public void addCategory(String name);
        public void showNext(int position);
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Communicator)
            hermes = (Communicator)context;
        else
            throw new ClassCastException();
    }
}