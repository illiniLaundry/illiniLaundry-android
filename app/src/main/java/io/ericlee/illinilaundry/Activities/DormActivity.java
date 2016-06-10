package io.ericlee.illinilaundry.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ericlee.illinilaundry.Adapters.DormAdapter;
import io.ericlee.illinilaundry.Adapters.GridAdapter;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.DormImages;
import io.ericlee.illinilaundry.Model.Machine;
import io.ericlee.illinilaundry.R;

public class DormActivity extends AppCompatActivity {

    private String name;
    private int wash;
    private int dry;
    private int inWash;
    private int inDry;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Machine> mDataset;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_left_out);
        setContentView(R.layout.activity_dorm);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        wash = intent.getIntExtra("Wash", 99);
        dry = intent.getIntExtra("Dry", 99);
        inWash = intent.getIntExtra("InWash", 99);
        inDry = intent.getIntExtra("InDry", 99);

        mDataset = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dorm);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitleEnabled(true);
        mCollapsingToolbarLayout.setTitle(name);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView image = (ImageView) findViewById(R.id.imageDorm);
        image.setImageResource(DormImages.getInstance().getImages().get(name));

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
                String url = getURL();
                Document illini = Jsoup.connect(url).get();

                Element table = illini.select("tbody").last();
                Elements rows = table.select("tr");
                rows.remove(0);
                rows.remove(rows.size() - 1);

                for (int i = 0; i < rows.size() - 1; i++) {
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

        private String getURL() {
            switch (name) {
                case "Allen":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=0&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Barton-Lundgren":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=1&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Bousfield Rm 103":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=2&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Busey-Evans":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=3&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Daniels North":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=4&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Daniels South":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=5&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "FAR: Oglesby":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=6&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "FAR: Trelease":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=7&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "300 South Goodwin":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=8&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "1107 West Green":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=9&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Hopkins":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=10&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "ISR: Townsend":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=11&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "ISR: Wardall":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=12&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "LAR: Leonard":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=13&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "LAR: Shelden":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=14&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Nugent":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=15&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Nugent Rm 126":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=16&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Orchard Downs North":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=17&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Orchard Downs South":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=18&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "PAR: Babcock":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=19&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "PAR: Blaisdell":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=20&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "PAR: Carr":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=21&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "PAR: Saunders":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=22&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Scott":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=23&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Sherman Short":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=24&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Sherman Tall":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=25&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Snyder":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=26&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "TVD: Taft":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=27&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "TVD: Van Doren":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=28&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                case "Weston":
                    return "https://www.laundryalert.com/cgi-bin/urba7723/LMRoom?XallingPage=LMPage&Halls=29&PreviousHalls=&RoomPersistence=&MachinePersistenceA=&MachinePersistenceB=";
                default:
                    return "";
            }
        }
    }
}


