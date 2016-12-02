package com.uos.leitner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
                CategoryAddTab1 tabFragment1 = new CategoryAddTab1();
                return tabFragment1;
            case 1:
                CategoryAddTab2 tabFragment2 = new CategoryAddTab2();
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
