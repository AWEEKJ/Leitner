package com.uos.leitner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.uos.leitner.helper.DatabaseHelper;
import com.uos.leitner.model.Category;

import java.util.ArrayList;

/**
 * Created by changhyeon on 2016. 10. 30..
 */

public class CategoryView extends Fragment {

    private Communicator hermes = null; // 인터페이스로 구현한 메소드 전달자 (MainActivity와 CategoryView사이에 정보 교환
    private int count = 0;
    private int clicked = 0;

    private ArrayList<Category> categoryList;
    private CategoryAdapter adapter;

    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        count = getActivity().getIntent().getIntExtra("count", count);
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
                getActivity().finish();
                Intent intent = new Intent(getActivity(), AddCategoryActivity.class);

                intent.putExtra("count", count);
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
                int i = 0;
                    Intent intent = new Intent(getActivity(), ModifyCategoryActivity.class);
                    intent.putExtra("categoryId", db_id);
                    getActivity().finish();
                    getActivity().startActivity(intent);

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
        super.onAttach(context);

        if(context instanceof Communicator)
            hermes = (Communicator)context;
        else
            throw new ClassCastException();
    }

    // MainActivity에 정보를 전달하기 위한 인터페이스. Hermes로 호출
    public interface Communicator {
        public void initialize(ArrayList<Category> list); // 앱 실행 시 DB 정보를 바탕으로 초기화
        public void showNext(int position); // 항목 클릭했을 때
        public void delete(int position); // 항목 삭제
    }
}