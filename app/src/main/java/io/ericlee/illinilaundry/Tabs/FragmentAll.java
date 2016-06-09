package io.ericlee.illinilaundry.Tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ArrayList<Dorm> mDataset;

    private ArrayList<ArrayList<String>> laundryData;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        view = inflater.inflate(R.layout.fragment_all, container, false);

        mDataset = new ArrayList<>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        setData();

        GridLayoutManager glm = new GridLayoutManager(this.getContext(), 2);
        mRecyclerView.setLayoutManager(glm);
        mAdapter = new GridAdapter(mDataset);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new SetData().execute();
            }
        });

        return view;
    }

    private void setData() {
        laundryData = new ArrayList<>();
        try {
            String url = "https://www.laundryalert.com/cgi-bin/urba7723/LMPage?Login=True";
            Document illini = Jsoup.connect(url).get();

            Element table = illini.select("tbody").get(2);
            Elements rows = table.select("tr");

            rows.remove(0);
            rows.remove(0);

            for (int i = 0; i < rows.size() - 1; i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                ArrayList<String> temp = new ArrayList<>();

                for (int j = 0; j < cols.size(); j++) {
                    if (j == 1) {
                        temp.add(cols.get(j).text());
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

                ArrayList<String> temp = laundryData.get(i);
                Log.i("temp", temp.toString());
                mDataset.add(new Dorm(temp.get(0),
                        Integer.parseInt(temp.get(1)),
                        Integer.parseInt(temp.get(2)),
                        Integer.parseInt(temp.get(3)),
                        Integer.parseInt(temp.get(4))));
            }
        } else {
            for (int i = 0; i < laundryData.size(); i++) {
                ArrayList<String> temp = laundryData.get(i);
                Log.i("temp", temp.toString());
                mDataset.set(i, new Dorm(temp.get(0),
                        Integer.parseInt(temp.get(1)),
                        Integer.parseInt(temp.get(2)),
                        Integer.parseInt(temp.get(3)),
                        Integer.parseInt(temp.get(4))));
            }
        }
    }

    public class SetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
