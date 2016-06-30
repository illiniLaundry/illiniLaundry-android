package io.ericlee.illinilaundry.Tabs;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.ericlee.illinilaundry.Adapters.BookmarkAdapter;
import io.ericlee.illinilaundry.Model.AppPreferences;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.R;

/**
 * Created by Eric on 6/8/2016.
 */
public class FragmentBookmarks extends Fragment {
    private AppPreferences preferences;

    public static ArrayList<Dorm> bookmarkedDorms;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        bookmarkedDorms = new ArrayList<>();
    }

    @Override
    public void onStart() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.bookmarkSwipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.bookmarkRecyclerView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new SetData().execute();
            }
        });

        LinearLayoutManager glm = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(glm);
        mAdapter = new BookmarkAdapter(bookmarkedDorms);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        if (bookmarkedDorms.isEmpty()) {
            // Delay to allow everything to settle before running SetData()
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new SetData().execute();
                }
            }, 500);
        }

        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
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
                mSwipeRefreshLayout.setRefreshing(true);
                new SetData().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("SetData", "RUN!");
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            preferences = AppPreferences.getInstance(getContext());
            Set<String> bookmarks = preferences.getBookmarkedDorms();
            Iterator<String> iterator = bookmarks.iterator();

            Log.i("Has Bookmarks", iterator.hasNext() + "");

            if(!iterator.hasNext()) {
                bookmarkedDorms.clear();
            }

            while (iterator.hasNext()) {
                String dormName = iterator.next();

                ArrayList<Dorm> temp = FragmentAll.getDorms();

                // TODO: We should find a faster way to do this because this will take O(n^m)

                for (int i = 0; i < temp.size(); i++) {
                    Dorm tempDorm = temp.get(i);

                    if (tempDorm.getName().equals(dormName) && !bookmarkedDorms.contains(tempDorm)) {
                        bookmarkedDorms.add(temp.get(i));
                    }

                    if(!bookmarks.contains(tempDorm.getName())) {
                        bookmarkedDorms.remove(tempDorm);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mAdapter.notifyDataSetChanged();
            // Notify swipeRefreshLayout that the refresh has finished
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}
