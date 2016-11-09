package com.uos.leitner;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;

import java.util.ArrayList;

/**
 * Created by changhyeon on 2016. 10. 30..
 */

public class CategoryView extends Fragment {

    private int count=1;    // 새로 생성되는 카테고리의 개수를 체크
    Communicator hermes = null; // 인터페이스로 구현한 메소드 전달자 (MainActivity와 CategoryView사이에 정보 교환

    private int clicked = 0;

    ArrayList<Category> categoryList;
    CategoryAdapter adapter;

    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_category, null);
        db = new DatabaseHelper(getContext());

        // ListView 생성
        categoryList = new ArrayList<Category>();
        adapter = new CategoryAdapter(this.getActivity(), R.layout.listview_category, categoryList);
        final ListView listView = (ListView)view.findViewById(R.id.ListView);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);   // 메뉴 입히기

        // CategoryView 구성요소
        final Button addButton = (Button)view.findViewById(R.id.addButton);
        final LinearLayout ly = (LinearLayout)view.findViewById(R.id.insertPopup);
        final Button insertButton = (Button)view.findViewById(R.id.insertButton);
        final EditText insertName = (EditText)view.findViewById(R.id.inputName);
//        final EditText maxTime = (EditText)view.findViewById(R.id.inputTime);
//        final EditText level = (EditText)view.findViewById(R.id.inputLevel);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        /**
         * 11.09 추가.
         *

         final Spinner spinner_time = (Spinner)findViewById(R.id.spinner_time);
         final Spinner spinner_level = (Spinner)findViewById(R.id.spinner_level);

         ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(

         this, R.array.Spinner_time, android.R.layout.simple_spinner_item);

         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

         spinner_time.setAdapter(adapter);



         ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(

         this, R.array.Spinner_level, android.R.layout.simple_spinner_item);

         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

         spinner_level.setAdapter(adapter2);

         */

        String[] optionLavala = getResources().getStringArray(R.array.SpinnerArray_time);
        ArrayAdapter<String> adapter_time = new ArrayAdapter<>
                (this.getActivity(), android.R.layout.simple_spinner_dropdown_item, optionLavala);
        final Spinner obj_time = (Spinner)view.findViewById(R.id.spinner_time);
        obj_time.setAdapter(adapter_time);


        String[] optionLavala_level = getResources().getStringArray(R.array.SpinnerArray_level);
        ArrayAdapter<String> adapter_level = new ArrayAdapter<>
                (this.getActivity(), android.R.layout.simple_spinner_dropdown_item, optionLavala_level);
        final Spinner obj_level = (Spinner)view.findViewById(R.id.spinner_level);
        obj_level.setAdapter(adapter_level);
//
//        obj_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });


        hermes.initialize(categoryList);    // DB로 부터 정보를 읽어와 listView를 초기화

        // 추가버튼을 클릭했을 때 (UI만 변화함)
        addButton.setOnClickListener(new View.OnClickListener() {   //항목 추가 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                ly.setVisibility(View.VISIBLE);
                insertName.requestFocus();
                imm.showSoftInput(insertName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // 추가 확인버튼 클릭했을 때
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                if(count <= ((MainActivity)getActivity()).MAX) {

                    // EditText에서 입력을 받아옴
                    String name = insertName.getText().toString();


//                    int goaltime = Integer.parseInt(maxTime.getText().toString());
//                    int currentLevel = Integer.parseInt(level.getText().toString());



                    int goaltime = obj_time.getSelectedItemPosition();
                    int currentLevel = obj_level.getSelectedItemPosition();

//                    int goaltime = Integer.parseInt(obj_time.getText().toString());
//                    int currentLevel = Integer.parseInt(obj_level.getText().toString());


                    // 입력받은 정보를 categoryList에 추가. 이때 listView 항목에 나타나게 됨
                    Category contents = new Category(name, goaltime, currentLevel);
                    categoryList.add(contents);

//                    insertName.setText(""); maxTime.setText(""); level.setText("");
//                    insertName.clearFocus();

                    // 생성된 항목 DB 저장 & 측정 페이지를 생성
                    hermes.addCategory(db.createCategory(contents));

                    count++;
                }

                else
                    Toast.makeText(getActivity(), "추가 가능한 개수 초과", Toast.LENGTH_LONG).show();
            }
        });

        // listView의 항목을 클릭했을 때. 해당 항목 페이지로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                hermes.showNext(position);
//                Toast.makeText(getActivity(), "페이저로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        // listView 항목을 길게 클릭했을 때. 메뉴 팝업
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                registerForContextMenu(parent);
                getActivity().openContextMenu(parent);

                clicked = position;

                return true;
            }
        });

        return view;
    }

    // MainActivity에 정보를 전달하기 위한 인터페이스. Hermes로 호출
    public interface Communicator {
        public void initialize(ArrayList<Category> list);
        public void addCategory(long id);//ArrayList<Category> categoryList, int position);
        public void showNext(int position);
        public void update(int position);
    }

    // hermes 생성
    public void onAttach(Context context) {
        super.onAttach(getActivity());

        if(context instanceof Communicator)
            hermes = (Communicator)context;
        else
            throw new ClassCastException();
    }

    // 메뉴창 생성
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.ListView) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    // 메뉴 선택시 액션
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.edit:
                Toast toast = Toast.makeText(getActivity(), Long.toString(categoryList.get(clicked).getSubject_ID()), Toast.LENGTH_SHORT );
                toast.show();
                return true;

            case R.id.delete:
                Log.d("DB", Long.toString(categoryList.get(clicked).getSubject_ID()));
                Log.d("CATEGORY VIEW", Integer.toString(clicked));

                db.deleteCategory(categoryList.get(clicked).getSubject_ID());
                adapter.removeItem(categoryList, clicked);
                hermes.update(clicked);

                Toast toast2 = Toast.makeText(getActivity(), "삭제", Toast.LENGTH_SHORT );
                toast2.show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /*
    public static CategoryView newInstance(){
        CategoryView fragment = new CategoryView();
        Bundle args =  new Bundle();
        fragment.setArguments(args);

        return fragment;
    }
    */
}