package io.ericlee.illinilaundry.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.ericlee.illinilaundry.Adapters.DormAdapter;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.DormImages;
import io.ericlee.illinilaundry.Model.Machine;
import io.ericlee.illinilaundry.R;

public class DormActivity extends AppCompatActivity {
    private Dorm dorm;
    private String statusAnnouncement;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Machine> mDataset;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private DormActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        overridePendingTransition(R.anim.slide_left, R.anim.slide_left_out);
        setContentView(R.layout.activity_dorm);

        Intent intent = getIntent();
        dorm = (Dorm) intent.getSerializableExtra("Dorm");
        mDataset = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dorm);
        setSupportActionBar(toolbar);


        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
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
        mAdapter = new DormAdapter(mDataset);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.dormSwipeRefresh);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_bookmark) {

            //TODO clean this code up...
            SharedPreferences settings = getSharedPreferences("BOOKMARKS", 0);
            Set<String> bookmarks = settings.getStringSet("bookmarks", null);

            if(!bookmarks.contains(dorm.getName())) {
                Set<String> newBookmarks = new HashSet<>(bookmarks);
                newBookmarks.add (dorm.getName());
                settings.edit().putStringSet("bookmarks",newBookmarks).commit();
            }
            else {
                Set<String> newBookmarks = new HashSet<>(bookmarks);
                newBookmarks.remove(dorm.getName());
                settings.edit().putStringSet("bookmarks",newBookmarks).commit();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            setData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mAdapter.notifyDataSetChanged();

            // Notify swipeRefreshLayout that the refresh has finished
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }

        private void setData() {
            ArrayList<ArrayList<String>> machineData = new ArrayList<>();

            try {
                Document illini = Jsoup.connect(dorm.getPageUrl()).get();

                Element table = illini.select("tbody").last();
                Elements rows = table.select("tr");


                int i=1;
                //check for announcement
                Element firstRow = rows.get(0);
                Elements firstRowCols = firstRow.select("td");
                if(firstRowCols.size()==3) {
                    statusAnnouncement = firstRowCols.get(2).text();
                    Log.i("announcement", statusAnnouncement);
                    i++;

                    //TODO get rid of this toast and display announcement as a textview or something instead
                    instance.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(instance,
                                    statusAnnouncement, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                for (; i < rows.size() - 2; i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");

                    ArrayList<String> temp = new ArrayList<>();

                    for (int j = 0; j < cols.size(); j++) {
                        if (j == 2) {
                            temp.add(cols.get(j).text());
                        }
                        if (j == 3) {
                            temp.add(cols.get(j).text());
                        }
                        if (j == 4) {
                            temp.add(cols.get(j).text());
                        }
                    }
                    machineData.add(temp);
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getBaseContext(),
                                "Connection error occurred. Try again later.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

                e.printStackTrace();
            }

            Log.i("temp", machineData.toString());
            if (mDataset.size() == 0) {
                mDataset.add(new Machine("#", "Machine Type", "Machine Status"));
                for (int i = 0; i < machineData.size(); i++) {
                    ArrayList<String> temp = machineData.get(i);
                    Log.i("temp", temp.toString());
                    mDataset.add(new Machine(temp.get(0),
                            temp.get(1),
                            temp.get(2)));
                }
            } else {
                for (int i = 0; i < machineData.size(); i++) {
                    ArrayList<String> temp = machineData.get(i);
                    Log.i("temp", temp.toString());
                    mDataset.set(i + 1, new Machine(temp.get(0),
                            temp.get(1),
                            temp.get(2)));
                }
            }
        }
    }
}


