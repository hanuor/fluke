package com.hanuor.fluke.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.hanuor.fluke.fragments.FirstScreenFrag;
import com.hanuor.fluke.fragments.SecondScreenFrag;

/**
 * Created by Shantanu Johri on 08-05-2016.
 */
public class ToolbarViewPager extends FragmentPagerAdapter {
    public ToolbarViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FirstScreenFrag();

            case 1:
                return new SecondScreenFrag();

            default:
                return new FirstScreenFrag();
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
