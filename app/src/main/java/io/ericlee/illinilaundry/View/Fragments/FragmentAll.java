package io.ericlee.illinilaundry.View.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.DormParser;
import io.ericlee.illinilaundry.View.Adapters.DormCardAdapter;
import io.ericlee.illinilaundry.View.ItemOffsetDecoration;
import io.ericlee.illinilaundry.R;

public class FragmentAll extends Fragment {
    private DormCardAdapter mAdapter;
    private ArrayList<Dorm> mDorms;

    @BindView(R.id.backgroundIllini) ImageView bgImage;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDorms = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout.setEnabled(false);

        GridLayoutManager glm = new GridLayoutManager(this.getContext(), 3);
        mRecyclerView.setLayoutManager(glm);

        mAdapter = new DormCardAdapter(mDorms, getContext());
        mRecyclerView.setAdapter(mAdapter);

        // Add spacing between cards.
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setHasFixedSize(true);

        // Populate Dorms
        new SetData().execute();
    }

    private class SetData extends AsyncTask<Void, Void, ArrayList<Dorm>> {
        @Override
        protected void onCancelled() {
            super.onCancelled();
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        }

        @Override
        protected ArrayList<Dorm> doInBackground(Void... voids) {
            try {
                ArrayList<Dorm> dorms = DormParser.getInstance().getData();
                return dorms;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Dorm> dorms) {
            if (dorms != null) {
                mAdapter.setItems(dorms);
                bgImage.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(getContext(), "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
            }

            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
