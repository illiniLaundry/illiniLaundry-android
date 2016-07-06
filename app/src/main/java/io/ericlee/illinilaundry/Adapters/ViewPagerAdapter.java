package io.ericlee.illinilaundry.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.ericlee.illinilaundry.Tabs.FragmentAlarms;
import io.ericlee.illinilaundry.Tabs.FragmentAll;
import io.ericlee.illinilaundry.Tabs.FragmentBookmarks;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentAlarms tab0 = new FragmentAlarms();
                return tab0;
            case 1:
                FragmentAll tab1 = new FragmentAll();
                return tab1;
            case 2:
                FragmentBookmarks tab2 = new FragmentBookmarks();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}