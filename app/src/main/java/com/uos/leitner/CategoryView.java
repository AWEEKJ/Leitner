package com.uos.leitner;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
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
        final RelativeLayout ry = (RelativeLayout)view.findViewById(R.id.insertPopup);
        final Button insertButton = (Button)view.findViewById(R.id.insertButton);
        final EditText insertName = (EditText)view.findViewById(R.id.inputName);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

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

        hermes.initialize(categoryList);    // DB로 부터 정보를 읽어와 listView를 초기화

        // 추가버튼을 클릭했을 때 (UI만 변화함)
        addButton.setOnClickListener(new View.OnClickListener() {   //항목 추가 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                ry.setVisibility(View.VISIBLE);
                insertName.requestFocus();
                imm.showSoftInput(insertName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // 추가 확인버튼 클릭했을 때
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ry.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                if(count <= ((MainActivity)getActivity()).MAX) {

                    // EditText에서 입력을 받아옴
                    String name = insertName.getText().toString();
                    int goal_tmp = obj_time.getSelectedItemPosition();
                    int current_tmp = obj_level.getSelectedItemPosition();

                    int goaltime = Integer.parseInt((String)obj_time.getAdapter().getItem(goal_tmp));
                    int currentLevel = Integer.parseInt((String)obj_level.getAdapter().getItem(current_tmp));


                    // 입력받은 정보를 categoryList에 추가. 이때 listView 항목에 나타나게 됨
                    Category contents = new Category(name, goaltime, currentLevel);
                    categoryList.add(contents);

                    insertName.setText("");
                    insertName.clearFocus();

                    obj_time.setSelection(0);
                    obj_level.setSelection(0);

                    // 생성된 항목 DB 저장 & 측정 페이지를 생성
                    hermes.addCategory(db.createCategory(contents));
                    hermes.refresh_List(categoryList);

                    count++;
                }

                else
                    Toast.makeText(getActivity(), "추가 가능한 개수 초과", Toast.LENGTH_LONG).show();
            }
        });

        // 항목을 클릭. 해당 페이지로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                hermes.showNext(position);
            }
        });

        // 항목을 길게 클릭. 메뉴 팝업
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
        int db_id = categoryList.get(clicked).getSubject_ID();

        switch(item.getItemId()) {
            case R.id.edit: // 항목 편집
                db.updateCategory(db_id, "편집테스트");

                hermes.refresh_List(categoryList);
                hermes.refresh_View(categoryList);
                adapter.notifyDataSetChanged();

                return true;

            case R.id.delete: // 항목 삭제
                adapter.removeItem(clicked); // 리스트뷰에서 지우기
                hermes.delete(clicked); // 페이지 지우기
                db.deleteCategory(db_id); // DB에서 지우기

                count--;

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    // hermes 생성
    public void onAttach(Context context) {
        super.onAttach(getActivity());

        if(context instanceof Communicator)
            hermes = (Communicator)context;
        else
            throw new ClassCastException();
    }

    // MainActivity에 정보를 전달하기 위한 인터페이스. Hermes로 호출
    public interface Communicator {
        public void initialize(ArrayList<Category> list); // 앱 실행 시 DB 정보를 바탕으로 초기화
        public void addCategory(long id); // 새로운 항목 추가
        public void refresh_List(ArrayList<Category> categoryList); // 새 항목 추가 후 업데이트
        public void refresh_View(ArrayList<Category> categoryList);
        public void showNext(int position); // 항목 클릭했을 때
        public void delete(int position); // 항목 삭제
    }
}