package com.uos.leitner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.uos.leitner.model.Category;

import java.util.ArrayList;

/**
 * Created by changhyeon on 2016. 10. 31..
 */


class MyPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> listFragment = new ArrayList<>();

    public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public int getItemPosition (Object object) {
        int index = listFragment.indexOf (object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();

        super.destroyItem(container, position, object);
    }

    // 동적으로 fragment를 생성
    public void add(Fragment f) {
        listFragment.add(f);
        notifyDataSetChanged();
    }

    // 동적으로 fragment를 삭제
    public void remove(int position) {
        listFragment.remove(position);
        notifyDataSetChanged();
    }

    public void remove_all(ArrayList<Category> categoryList) {

//        while(listFragment.size()>1) {
//            listFragment.remove(listFragment.size());
//            notifyDataSetChanged();
//        }
//        for (int i =0; i <zzz; i++) {
//            Log.d("i", Integer.toString(i));
//            Log.d("frag", Integer.toString(listFragment.size()));
//            Log.d("size", Integer.toString(zzz));
//
//            listFragment.remove(i);
//            notifyDataSetChanged();
//        }

    }
}
