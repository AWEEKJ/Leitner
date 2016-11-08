package com.uos.leitner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

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
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();

        super.destroyItem(container, position, object);
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    // 동적으로 fragment를 추가하기 위해 필요한 부분
    public void add(Fragment f) {
        listFragment.add(f);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        listFragment.remove(position);
        notifyDataSetChanged();
    }
}
