package com.uos.leitner.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.uos.leitner.ui.fragment.CategoryAddTab1Fragment;
import com.uos.leitner.ui.fragment.CategoryAddTab2Fragment;

/**
 * Created by HANJU on 2016. 12. 2..
 */

public class CategoryAddTabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public CategoryAddTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                CategoryAddTab1Fragment tabFragment1 = new CategoryAddTab1Fragment();
                return tabFragment1;
            case 1:
                CategoryAddTab2Fragment tabFragment2 = new CategoryAddTab2Fragment();
                return tabFragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
