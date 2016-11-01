package com.uos.leitner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by changhyeon on 2016. 10. 31..
 */

class MyPagerAdapter extends FragmentPagerAdapter { //FragmentStatePagerAdpater
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

    public void add(Fragment f) {
        listFragment.add(f);
        notifyDataSetChanged();
    }
}

    /* 혹시 몰라
    private ArrayList<WeakReference<Fragment>> registeredFragments = new ArrayList<WeakReference<Fragment>>();

    public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        WeakReference<Fragment> weakReference = registeredFragments.get(position);
        return weakReference.get();
    }

    @Override
    public int getCount() {
        return registeredFragments.size();
    }

    public void add(Fragment f){
        registeredFragments.add(new WeakReference<Fragment>(f));
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO View를 추가할때 호출됨
        // View를 리턴합니다.
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO View를 삭제할때 호출됨
        super.destroyItem(container, position, object);
    }
}
    */