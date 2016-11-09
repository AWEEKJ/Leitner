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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;

import java.util.ArrayList;

/**
 * Created by changhyeon on 2016. 10. 30..
 */

public class CategoryView extends Fragment {

    private DatabaseHelper db;
    private ArrayList<Category> categoryList;
    private CategoryAdapter adapter;
    private Communicator hermes = null; // 인터페이스 메소드 전달자 (MainActivity와 CategoryView사이에 정보 교환)

    private int count = 0; // 생성된 카테고리의 수 체크
    private int clicked = 0; // 편집할 때 몇 번째 리스트가 눌렸는지

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        categoryList = new ArrayList<Category>();
        adapter = new CategoryAdapter(this.getActivity(), R.layout.listview_category, categoryList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_category, null);
        db = new DatabaseHelper(getContext());

        // ListView 생성
        final ListView listView = (ListView)view.findViewById(R.id.ListView);
        listView.setAdapter(adapter);
        registerForContextMenu(listView); // 메뉴 입히기

        // CategoryView 구성요소 생성
        final Button addButton = (Button)view.findViewById(R.id.addButton);
        final LinearLayout ly = (LinearLayout)view.findViewById(R.id.insertPopup);
        final Button insertButton = (Button)view.findViewById(R.id.insertButton);
        final EditText insertName = (EditText)view.findViewById(R.id.inputName);
        final EditText maxTime = (EditText)view.findViewById(R.id.inputTime);
        final EditText level = (EditText)view.findViewById(R.id.inputLevel);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        hermes.initialize(categoryList); // DB로 부터 정보를 읽어와서 listView를 초기화

        // 추가버튼을 클릭 (UI만 변화)
        addButton.setOnClickListener(new View.OnClickListener() {   //항목 추가 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                ly.setVisibility(View.VISIBLE);
                insertName.requestFocus();
                imm.showSoftInput(insertName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // 추가 확인버튼 클릭
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);

                if(count < ((MainActivity)getActivity()).MAX) {

                    // EditText에서 입력을 받아옴
                    String name = insertName.getText().toString();
                    int goaltime = Integer.parseInt(maxTime.getText().toString());
                    int currentLevel = Integer.parseInt(level.getText().toString());

                    // 입력받은 정보를 categoryList에 추가. 이때 listView 항목에 나타나게 됨
                    Category contents = new Category(name, goaltime, currentLevel);
                    categoryList.add(contents);

                    // 생성된 항목 DB 저장 & 측정 페이지를 생성
                    hermes.addCategory(db.createCategory(contents));
                    hermes.refresh(categoryList);

                    insertName.setText(""); maxTime.setText(""); level.setText("");
                    insertName.clearFocus();

                    count++;
                }

                else
                    Toast.makeText(getActivity(), "생성 가능한 개수 초과", Toast.LENGTH_SHORT).show();
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
        switch(item.getItemId()) {
            case R.id.edit: // 항목 편집

                return true;

            case R.id.delete: // 항목 삭제
                int idx = categoryList.get(clicked).getSubject_ID();
                adapter.removeItem(clicked); // 리스트뷰에서 지우기
                hermes.delete(clicked); // 페이지 지우기
                db.deleteCategory(idx); // DB에서 지우기

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
        public void refresh(ArrayList<Category> categoryList); // 새 항목 추가 후 업데이트
        public void showNext(int position); // 항목 클릭했을 때
        public void delete(int position); // 항목 삭제
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