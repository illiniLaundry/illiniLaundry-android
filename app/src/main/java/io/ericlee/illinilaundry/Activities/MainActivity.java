package io.ericlee.illinilaundry.Activities;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Adapters.GridAdapter;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.LaundryData;
import io.ericlee.illinilaundry.R;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Dorm> mDataset;

    private ArrayList<ArrayList<String>> laundryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        //Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        setData();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        handleRefresh();
    }

    private void handleRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
    }

    private void refreshItems() {
        // Load items
        setData(); // TODO: Make this notify, so it's asynchronous

        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setData() {
        laundryData = LaundryData.getInstance().getData();
        mDataset = new ArrayList<>();

        for(int i = 0; i < laundryData.size(); i++) {

            ArrayList<String> temp = laundryData.get(i);
            Log.i("temp", temp.toString());
            mDataset.add(new Dorm(temp.get(0),
                    Integer.parseInt(temp.get(1)),
                    Integer.parseInt(temp.get(2)),
                    Integer.parseInt(temp.get(3)),
                    Integer.parseInt(temp.get(4))));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager glm = new GridLayoutManager(this, 2);

        mAdapter = new GridAdapter(mDataset);

        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static Context getContext() {
        return context;
    }
}
