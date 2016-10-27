package com.uos.leitner;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리스트뷰 보여주기
        final ListView listView = (ListView)findViewById(R.id.List);
        final ArrayList<Category> List = new ArrayList<Category>();

        final CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_list, List);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long l) {
                String str = listView.getItemAtPosition(position).toString();

                Toast toast = Toast.makeText(getApplicationContext(), "아무거도 안되지롱 " + str, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //항목 추가
        final Button addButton = (Button) findViewById(R.id.addButton);
        final Button inputButton = (Button) findViewById(R.id.inputButton);
        final EditText inputName = (EditText) findViewById(R.id.inputName);
        final LinearLayout ly = (LinearLayout) findViewById(R.id.inputPopup);
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ly.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.INVISIBLE);
                inputName.requestFocus();
                imm.showSoftInput(inputName, InputMethodManager.SHOW_IMPLICIT); //키보드
            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = inputName.getText().toString();

                Category temp = new Category(name, "0분");
                List.add(temp);

                ly.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                inputName.setText("");
                inputName.clearFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
    }

    private class CategoryAdapter extends ArrayAdapter<Category> {
        private ArrayList<Category> items;

        public CategoryAdapter(Context context, int textViewResourceId, ArrayList<Category> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.category_list, null);
            }

            Category c = items.get(position);

            if (c != null) {
                TextView tt = (TextView) v.findViewById(R.id.Category_Name);
                TextView bt = (TextView) v.findViewById(R.id.Category_Time);
                if (tt != null)
                    tt.setText(c.getName());
                if (bt != null)
                    bt.setText("집중 시간: " + c.getTime());
            }
            return v;
        }
    }
}
