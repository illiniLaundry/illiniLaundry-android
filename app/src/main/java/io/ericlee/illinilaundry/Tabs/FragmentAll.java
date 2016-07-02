package io.ericlee.illinilaundry.Tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Adapters.GridAdapter;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.R;

public class FragmentAll extends Fragment {

    //TODO: Get rid of Logs

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private static ArrayList<Dorm> mDataset;
    private String statusAnnouncement = "";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onStart() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new SetData().execute();
            }
        });

        if(mDataset.isEmpty()) {
            new SetData().execute();
        }

        GridLayoutManager glm = new GridLayoutManager(this.getContext(), 3);
        mRecyclerView.setLayoutManager(glm);
        mAdapter = new GridAdapter(mDataset);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mDataset = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_refresh:
                new SetData().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<Dorm> getDorms() { return mDataset; }

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

            ImageView bgImage = (ImageView) getView().findViewById(R.id.backgroundIllini);

            if(mDataset.isEmpty()) {
                bgImage.setVisibility(View.VISIBLE);
            } else {
                bgImage.setVisibility(View.INVISIBLE);
            }

            // Notify swipeRefreshLayout that the refresh has finished
            mSwipeRefreshLayout.setRefreshing(false);
        }

        private void setData() {
            ArrayList<ArrayList<String>> laundryData = new ArrayList<>();

            try {
                String url = "https://www.laundryalert.com/cgi-bin/urba7723/LMPage?Login=True";
                Document illini = Jsoup.connect(url).get();

                Element table = illini.select("tbody").get(2);
                Elements rows = table.select("tr");


                int i=1;
                //check for announcement
                Element firstRow = rows.get(0);
                Elements firstRowCols = firstRow.select("td");
                if(firstRowCols.size()==2) {
                    statusAnnouncement = firstRowCols.get(1).text();
                    Log.i("announcement", statusAnnouncement);
                    i++;

                    //TODO get rid of this toast and display announcement as a textview or something instead
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getContext(),
                                    statusAnnouncement, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }

                for (; i < rows.size() - 1; i++) {
                    Element row = rows.get(i);
                    Elements cols = row.select("td");

                    ArrayList<String> temp = new ArrayList<>();

                    for (int j = 0; j < cols.size(); j++) {
                        if (j == 1) {
                            temp.add(cols.get(j).text());
                            temp.add("https://www.laundryalert.com/cgi-bin/urba7723/"+cols.get(j).attr("onclick").split("href=")[1].replace("'",""));
                        }
                        if (j == 2) {
                            temp.add(cols.get(j).text());
                        }
                        if (j == 3) {
                            temp.add(cols.get(j).text());
                        }
                        if (j == 5) {
                            temp.add(cols.get(j).text());
                        }
                        if (j == 7) {
                            temp.add(cols.get(j).text());
                        }
                    }
                    laundryData.add(temp);
                }
            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getContext(),
                                "Connection error occurred. Try again later.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

                e.printStackTrace();
            }

            if (mDataset.size() == 0) {
                for (int i = 0; i < laundryData.size(); i++) {

                    ArrayList<String> dormData = laundryData.get(i);
                    Log.i("dormData", dormData.toString());
                    mDataset.add(new Dorm(dormData.get(0),
                            dormData.get(1),
                            Integer.parseInt(dormData.get(2)),
                            Integer.parseInt(dormData.get(3)),
                            Integer.parseInt(dormData.get(4)),
                            Integer.parseInt(dormData.get(5))));
                }
            } else {
                for (int i = 0; i < laundryData.size(); i++) {
                    ArrayList<String> temp = laundryData.get(i);
                    Log.i("dormData", temp.toString());
                    mDataset.set(i, new Dorm(temp.get(0),
                            temp.get(1),
                            Integer.parseInt(temp.get(2)),
                            Integer.parseInt(temp.get(3)),
                            Integer.parseInt(temp.get(4)),
                            Integer.parseInt(temp.get(5))));
                }
            }
        }
    }
}
