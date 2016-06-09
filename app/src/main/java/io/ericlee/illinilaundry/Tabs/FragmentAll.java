package io.ericlee.illinilaundry.Tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Adapters.GridAdapter;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.LaundryData;
import io.ericlee.illinilaundry.R;

public class FragmentAll extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Dorm> mDataset;

    private ArrayList<ArrayList<String>> laundryData;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        mDataset = new ArrayList<>();

        setData();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager glm = new GridLayoutManager(this.getContext(), 2);
        mRecyclerView.setLayoutManager(glm);
        mAdapter = new GridAdapter(mDataset);

        mRecyclerView.setAdapter(mAdapter);

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
        laundryData = LaundryData.getInstance().getData();

        for(int i = 0; i < laundryData.size(); i++) {

            ArrayList<String> temp = laundryData.get(i);
            Log.i("temp", temp.toString());
            mDataset.add(new Dorm(temp.get(0),
                    Integer.parseInt(temp.get(1)),
                    Integer.parseInt(temp.get(2)),
                    Integer.parseInt(temp.get(3)),
                    Integer.parseInt(temp.get(4))));
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
            //Here you can update the view
            mAdapter.notifyDataSetChanged();
            // Notify swipeRefreshLayout that the refresh has finished
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
