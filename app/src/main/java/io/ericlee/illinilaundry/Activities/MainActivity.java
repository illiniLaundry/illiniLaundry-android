package io.ericlee.illinilaundry.Activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.LinearLayout;

import io.ericlee.illinilaundry.Adapters.ViewPagerAdapter;
import io.ericlee.illinilaundry.R;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.icon);

        setSupportActionBar(toolbar);

        // Setup tabs
        TabLayout tab_layout = (TabLayout) findViewById(R.id.main_tab_layout);
        tab_layout.addTab(tab_layout.newTab().setText("All Dorms"));
        tab_layout.addTab(tab_layout.newTab().setText("Bookmarked"));

        final ViewPager view_pager = (ViewPager) findViewById(R.id.pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter
                (getSupportFragmentManager(), tab_layout.getTabCount());

        view_pager.setAdapter(adapter);

        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());
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

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Context getContext() {
        return context;
    }
}
