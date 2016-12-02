package com.uos.leitner.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by changhyeon on 2016. 10. 31..
 */

public class CategoryListPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> listFragment = new ArrayList<>();

    public CategoryListPagerAdapter(android.support.v4.app.FragmentManager fm) {
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
        int index = listFragment.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;

//        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();

//        trans.commitNow();

        super.destroyItem(container, position, object);
    }

    // 동적으로 fragment를 생성
    public void add(Fragment f) {
        listFragment.add(f);
        notifyDataSetChanged();
    }

//    // 동적으로 fragment를 삭제
    public void remove(int position) {
        listFragment.remove(position);
        notifyDataSetChanged();
    }
}
