package io.ericlee.illinilaundry.View.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.ericlee.illinilaundry.View.Fragments.FragmentAll;
import io.ericlee.illinilaundry.View.Fragments.FragmentBookmarks;

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
                FragmentAll tab1 = new FragmentAll();
                return tab1;
            case 1:
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