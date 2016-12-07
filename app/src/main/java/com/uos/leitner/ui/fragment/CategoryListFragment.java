package com.uos.leitner.ui.fragment;

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
import android.widget.ListView;
import android.widget.Toast;

import com.uos.leitner.R;
import com.uos.leitner.database.DatabaseHelper;
import com.uos.leitner.model.Category;
import com.uos.leitner.ui.activity.CategoryAddActivity;
import com.uos.leitner.ui.activity.CategoryEditActivity;
import com.uos.leitner.ui.activity.SettingListActivity;
import com.uos.leitner.ui.adapter.CategoryListAdapter;

import java.util.ArrayList;

/**
 * Created by changhyeon on 2016. 10. 30..
 */

public class CategoryListFragment extends Fragment{

    private Communicator hermes = null; // 인터페이스로 구현한 메소드 전달자 (MainActivity와 CategoryView사이에 정보 교환
    private int clicked = 0;
    private int categoryNum;

    private ArrayList<Category> categoryList;
    private CategoryListAdapter adapter;

    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_category_list, null);
        db = new DatabaseHelper(getContext());

        // ListView 생성
        categoryList = new ArrayList<Category>();

        adapter = new CategoryListAdapter(this.getActivity(), R.layout.item_category_list, categoryList);
        final ListView listView = (ListView)view.findViewById(R.id.ListView);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);   // 메뉴 입히기


        hermes.initialize(categoryList);    // DB로 부터 정보를 읽어와 listView를 초기화

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
                Intent intent = new Intent(getActivity(), CategoryEditActivity.class);
                intent.putExtra("categoryId", db_id);
                getActivity().finish();
                getActivity().startActivity(intent);
                return true;

            case R.id.delete: // 항목 삭제
                adapter.removeItem(clicked); // 리스트뷰에서 지우기
                hermes.delete(clicked); // 페이지 지우기
                db.deleteCategory(db_id); // DB에서 지우기
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
        void initialize(ArrayList<Category> list); // 앱 실행 시 DB 정보를 바탕으로 초기화
        void showNext(int position); // 항목 클릭했을 때
        void delete(int position); // 항목 삭제
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.newCategory) {
            categoryNum = categoryList.size();
            if (categoryNum == 5){
                Toast.makeText(getActivity(), "최대 카테고리 개수는 5개입니다.", Toast.LENGTH_LONG).show();
            } else {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), CategoryAddActivity.class);
                getActivity().startActivity(intent);
            }
        } else if (id == R.id.settings) {
            Intent intent = new Intent(getActivity(), SettingListActivity.class);
            getActivity().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}