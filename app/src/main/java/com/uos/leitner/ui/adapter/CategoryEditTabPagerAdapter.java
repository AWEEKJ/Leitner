package com.uos.leitner.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.uos.leitner.ui.fragment.CategoryEditTab1Fragment;
import com.uos.leitner.ui.fragment.CategoryEditTab2Fragment;

/**
 * Created by HANJU on 2016. 12. 7..
 */

public class CategoryEditTabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;
    private int categoryId;

    public CategoryEditTabPagerAdapter(FragmentManager fm, int tabCount, int categoryId) {
        super(fm);
        this.tabCount = tabCount;
        this.categoryId = categoryId;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        switch (position) {
            case 0:
                CategoryEditTab1Fragment tabFragment1 = CategoryEditTab1Fragment.newInstance(categoryId);
                return tabFragment1;
            case 1:
                CategoryEditTab2Fragment tabFragment2 = CategoryEditTab2Fragment.newInstance(categoryId);
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
