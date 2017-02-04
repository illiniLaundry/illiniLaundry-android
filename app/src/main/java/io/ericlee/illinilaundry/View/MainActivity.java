package io.ericlee.illinilaundry.View;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import io.ericlee.illinilaundry.Adapters.ViewPagerAdapter;
import io.ericlee.illinilaundry.Model.TinyDB;
import io.ericlee.illinilaundry.R;

public class MainActivity extends AppCompatActivity {

    private TinyDB preferences;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = TinyDB.getInstance(this);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.icon);

        setSupportActionBar(toolbar);

        // Setup tabs
        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Alarms"));
        tabLayout.addTab(tabLayout.newTab().setText("All Dorms"));
        tabLayout.addTab(tabLayout.newTab().setText("My Dorms"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        if(!preferences.getListString("bookmarkeddorms").isEmpty()) {
            TabLayout.Tab bookmarksTab = tabLayout.getTabAt(2);
            bookmarksTab.select();
            viewPager.setCurrentItem(2);
        } else {
            TabLayout.Tab allDormsTab = tabLayout.getTabAt(1);
            allDormsTab.select();
            viewPager.setCurrentItem(1);
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Have the back button go to "All" tab if "Bookmarks" tab is selected.
        TabLayout.Tab firstTab = tabLayout.getTabAt(0);
        if(firstTab.isSelected()) {
            super.onBackPressed();
        } else {
            firstTab.select();
        }
    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
