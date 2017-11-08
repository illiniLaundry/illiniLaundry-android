package io.ericlee.illinilaundry.View.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.DormInformation;
import io.ericlee.illinilaundry.Model.DormParser;
import io.ericlee.illinilaundry.Model.Machine;
import io.ericlee.illinilaundry.Utils.TinyDB;
import io.ericlee.illinilaundry.R;
import io.ericlee.illinilaundry.View.Adapters.MachineAdapter;

public class DormActivity extends AppCompatActivity {
    private Dorm dorm;

    private MachineAdapter mAdapter;
    private ArrayList<Machine> mDataset;


    @BindView(R.id.dormRecyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.dormSwipeRefresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.dormWasherAvailable) TextView availableWash;
    @BindView(R.id.dormDryerAvailable) TextView availableDry;
    @BindView(R.id.dorm_collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;

    private TinyDB preferences;
    private ArrayList<String> bookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_left_out);
        setContentView(R.layout.activity_dorm);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        dorm = (Dorm) intent.getSerializableExtra("Dorm");

        mDataset = new ArrayList<>();
        if (dorm.getMachines() != null) {
            Collections.addAll(mDataset, dorm.getMachines());
        }

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
        image.setImageResource(DormInformation.getInstance().getImages().get(dorm.getName()));

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new MachineAdapter(mDataset, this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new SetData().execute();
            }
        });

        if (mDataset.isEmpty()) {
            new SetData().execute();
        }
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
                new SetData().execute();
                return true;
            case R.id.action_bookmark:
                bookmarks = preferences.getListString("bookmarkeddorms");
                ArrayList<String> newBookmarks = new ArrayList<>(bookmarks);

                if(!bookmarks.contains(dorm.getName())) {
                    newBookmarks.add(dorm.getName());

                    item.setIcon(R.drawable.ic_star_yellow_24dp);

                    Toast.makeText(this, dorm.getName() + " has been added to your bookmarks.",
                            Toast.LENGTH_SHORT).show();
                } else {
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
        int freeDriers = 0;
        int freeWashers = 0;

        for(Machine m : dorm.getMachines()) {
            if (m.getType().contains("WASHER") && (m.getStatus().equals("Available")
                    || m.getTimeRemaining().contains("door still closed"))) {
                freeWashers++;
            }

            if (!m.getType().contains("WASHER") && m.getStatus().equals("Available")
                    || m.getTimeRemaining().contains("door still closed")) {
                freeDriers++;
            }
        }

        availableDry.setText("Dryers Available: " + freeDriers);
        availableWash.setText("Washers Available: " + freeWashers);
    }

    class SetData extends AsyncTask<Void, Void, Dorm> {
        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onCancelled() {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected Dorm doInBackground(Void... voids) {
            try {
                Dorm d = DormParser.getInstance().getDormData(dorm.getID());
                return d;
            } catch (Exception e) {
                // e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Dorm d) {
            if (d != null) {
                mAdapter.setItems(Arrays.asList(d.getMachines()));
                dorm = d;
                setAvailabilityText();
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
            }

            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}


