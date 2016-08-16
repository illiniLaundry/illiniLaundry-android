package io.ericlee.illinilaundry.Tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import io.ericlee.illinilaundry.Adapters.BookmarkAdapter;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.ItemOffsetDecoration;
import io.ericlee.illinilaundry.Model.MainParser;
import io.ericlee.illinilaundry.Model.TinyDB;
import io.ericlee.illinilaundry.R;

/**
 * Created by Eric on 6/8/2016.
 */
public class FragmentBookmarks extends Fragment {
    private TinyDB preferences;

    public static ArrayList<Dorm> bookmarkedDorms;

    private RecyclerView mRecyclerView;
    private BookmarkAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView bgImage;

    private ArrayList<Dorm> allDorms;

    private boolean firstTimeRun;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //TODO: GETT RID OF THIS
        allDorms = MainParser.getInstance().getDorms();

        preferences = TinyDB.getInstance(getContext());
        firstTimeRun = true;
        bookmarkedDorms = new ArrayList<>();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (bookmarkedDorms.isEmpty()) {
            // Delay to allow everything to settle before running SetData()
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new SetData().execute();
                }
            }, 500);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        bgImage = (ImageView) view.findViewById(R.id.backgroundIlliniBookmarks);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bookmarkSwipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.bookmarkRecyclerView);

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

        // Add spacing between cards.
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        // Extend the Callback class
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                ArrayList<String> forSwappingPreferences = preferences.getListString("bookmarkeddorms");

                // Swap positions
                Collections.swap(bookmarkedDorms, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Collections.swap(forSwappingPreferences, viewHolder.getAdapterPosition(), target.getAdapterPosition());

                preferences.putListString("bookmarkeddorms", forSwappingPreferences);

                // Notify the adapter
                mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // TODO: Doesn't work for some reason. Maybe conflicting listeners?
                mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
                Log.i("item Dismiss", "CALL2");
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };

        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        ItemTouchHelper ith = new ItemTouchHelper(callback);
        ith.attachToRecyclerView(mRecyclerView);

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
            preferences = TinyDB.getInstance(getContext());
            ArrayList<String> bookmarks = preferences.getListString("bookmarkeddorms");

            Log.i("Has Bookmarks", !bookmarks.isEmpty() + "");

            if (bookmarks.isEmpty()) {
                bookmarkedDorms.clear();
            }

            MainParser.getInstance().getDorms();

            for(int i = 0; i < bookmarks.size(); i++) {
                String dormName = bookmarks.get(i);

                // TODO: We should find a faster way to do this because this will take O(n^m)

                for (int j = 0; j < allDorms.size(); j++) {
                    Dorm tempDorm = allDorms.get(j);

                    if (tempDorm.getName().equals(dormName) && !bookmarkedDorms.contains(tempDorm)) {
                        bookmarkedDorms.add(allDorms.get(j));
                    }

                    if (!bookmarks.contains(tempDorm.getName())) {
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

            if (bookmarkedDorms.isEmpty()) {
                bgImage.setVisibility(View.VISIBLE);
            } else {
                bgImage.setVisibility(View.INVISIBLE);
            }

            if (firstTimeRun) {
                firstTimeRun = false;
            } else if (allDorms.isEmpty() && !firstTimeRun) {
                Toast.makeText(getContext(), "Try refreshing the \"All Dorms\" tab first.",
                        Toast.LENGTH_LONG).show();
                Log.i("firsttimerun", firstTimeRun + "");
            }

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
