package io.ericlee.illinilaundry.Tabs;

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

import io.ericlee.illinilaundry.Adapters.GridAdapter;
import io.ericlee.illinilaundry.Model.ItemOffsetDecoration;
import io.ericlee.illinilaundry.Model.MainParser;
import io.ericlee.illinilaundry.R;

public class FragmentAll extends Fragment {
    private RecyclerView.Adapter mAdapter;
    private MainParser mainParser;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView bgImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        bgImage = (ImageView) view.findViewById(R.id.backgroundIllini);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        GridLayoutManager glm = new GridLayoutManager(this.getContext(), 3);
        mRecyclerView.setLayoutManager(glm);

        mAdapter = new GridAdapter(MainParser.getInstance().getDorms());

        mainParser = MainParser.getInstance();

        if(mainParser.getDorms().isEmpty()) {
            mainParser.parse(mSwipeRefreshLayout, mAdapter, bgImage);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mainParser.parse(mSwipeRefreshLayout, mAdapter, bgImage);
            }
        });

        // Add spacing between cards.
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mainParser.parse(mSwipeRefreshLayout, mAdapter, bgImage);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
