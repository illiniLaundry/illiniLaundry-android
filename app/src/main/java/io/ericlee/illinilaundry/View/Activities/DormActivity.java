package io.ericlee.illinilaundry.View.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.DormImages;
import io.ericlee.illinilaundry.Model.Machine;
import io.ericlee.illinilaundry.Utils.TinyDB;
import io.ericlee.illinilaundry.R;

public class DormActivity extends AppCompatActivity {
    private Dorm dorm;
    private String statusAnnouncement;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Machine> mDataset;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private DormActivity instance;

    private TextView availableWash;
    private TextView availableDry;

    private TinyDB preferences;
    private ArrayList<String> bookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        overridePendingTransition(R.anim.slide_left, R.anim.slide_left_out);
        setContentView(R.layout.activity_dorm);

        availableWash = (TextView) findViewById(R.id.dormWasherAvailable);
        availableDry = (TextView) findViewById(R.id.dormDryerAvailable);
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.dorm_collapsing_toolbar);

        Intent intent = getIntent();
        dorm = (Dorm) intent.getSerializableExtra("Dorm");

        setAvailabilityText();

        mDataset = new ArrayList<>();

        preferences = TinyDB.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dorm);
        setSupportActionBar(toolbar);

        // Check if the version of Android is Lollipop or higher
        if (Build.VERSION.SDK_INT >= 21) {
            // Set the status bar to dark-semi-transparentish
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setTitle(dorm.getName());

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView image = (ImageView) findViewById(R.id.imageDorm);
        image.setImageResource(DormImages.getInstance().getImages().get(dorm.getName()));

        mRecyclerView = (RecyclerView) findViewById(R.id.dormRecyclerView);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //mAdapter = new DormAdapter(mDataset);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.dormSwipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);


        if (mDataset.isEmpty()) {
            //new SetData().execute();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bookmark, menu);

        bookmarks = preferences.getListString("bookmarkeddorms");

        if(bookmarks.contains(dorm.getName())) {
            menu.getItem(0).setIcon(R.drawable.ic_star_yellow_24dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_star_border_white_24dp);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                //mSwipeRefreshLayout.setRefreshing(true);
                //new SetData().execute();
                return true;
            case R.id.action_bookmark:
                // TODO: clean this code up...
                ArrayList<String> newBookmarks = new ArrayList<>(bookmarks);
                bookmarks = preferences.getListString("bookmarkeddorms");

                if(!bookmarks.contains(dorm.getName())) {
                    newBookmarks.add(dorm.getName());

                    item.setIcon(R.drawable.ic_star_yellow_24dp);

                    Toast.makeText(this, dorm.getName() + " has been added to your bookmarks.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    newBookmarks.remove(dorm.getName());

                    item.setIcon(R.drawable.ic_star_border_white_24dp);

                    Toast.makeText(this, dorm.getName() + " has been removed from your bookmarks.",
                            Toast.LENGTH_SHORT).show();
                }

                preferences.putListString("bookmarkeddorms", newBookmarks);
                return true;
            default:
                // Do nothing. This is just in case something bad happens.
        }

        return super.onOptionsItemSelected(item);
    }

    private void setAvailabilityText() {
        //availableDry.setText("Dryers Available: " + dorm.getDry());
        //availableWash.setText("Washers Available: " + dorm.getWash());
    }
}


