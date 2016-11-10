package com.uos.leitner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

        hermes.initialize(categoryList);    // DB로 부터 정보를 읽어와 listView를 초기화

        // 추가버튼을 클릭했을 때 새로운 창 생성
        addButton.setOnClickListener(new View.OnClickListener() {   //항목 추가 버튼 눌렀을 때
            @Override
            public void onClick(View view) {
                Log.d("Clicked button", "fragmentReplace");

                Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
                getActivity().startActivity(intent);
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
                adapter.notifyDataSetChanged();

                return true;

            case R.id.delete: // 항목 삭제
                adapter.removeItem(clicked); // 리스트뷰에서 지우기
                hermes.delete(clicked); // 페이지 지우기
                db.deleteCategory(db_id); // DB에서 지우기

                //count--;

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    // hermes 생성
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Communicator)
            hermes = (Communicator)context;
        else
            throw new ClassCastException();
    }

    // MainActivity에 정보를 전달하기 위한 인터페이스. Hermes로 호출
    public interface Communicator {
        public void initialize(ArrayList<Category> list); // 앱 실행 시 DB 정보를 바탕으로 초기화
        public void refresh_List(ArrayList<Category> categoryList); // 새 항목 추가 후 업데이트
        public void showNext(int position); // 항목 클릭했을 때
        public void delete(int position); // 항목 삭제
    }
}