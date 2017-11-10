package io.ericlee.illinilaundry.View.Fragments;

import android.media.Image;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ericlee.illinilaundry.Model.DormInformation;
import io.ericlee.illinilaundry.Model.DormParser;
import io.ericlee.illinilaundry.View.Adapters.BookmarkAdapter;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.View.ItemOffsetDecoration;
import io.ericlee.illinilaundry.Utils.TinyDB;
import io.ericlee.illinilaundry.R;

/**
 * Created by Eric on 6/8/2016.
 */
public class FragmentBookmarks extends Fragment {
    private TinyDB preferences;

    public static ArrayList<Dorm> bookmarkedDorms;

    @BindView(R.id.bookmarkRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.bookmarkSwipeRefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.backgroundIlliniBookmarks)
    ImageView bgImage;

    private BookmarkAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        preferences = TinyDB.getInstance(getContext());
        bookmarkedDorms = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ButterKnife.bind(this, view);
        Log.d("OnCreateView", "Called!");
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new SetData().execute();
            }
        });

        LinearLayoutManager glm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(glm);

        mAdapter = new BookmarkAdapter(getContext(), bookmarkedDorms);

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
                // Empty
            }

            // Defines the enabled move directions in each state (idle, swiping, dragging).
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
    public void onResume() {
        super.onResume();

        // Delay the SetData call because there's a bug if we don't when we create the fragment for the first time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new SetData().execute();
            }
        }, 100);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                new SetData().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SetData extends AsyncTask<Void, Void, ArrayList<Dorm>> {
        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }

        @Override
        protected void onCancelled() {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected ArrayList<Dorm> doInBackground(Void... params) {
            preferences = TinyDB.getInstance(getContext());
            ArrayList<String> bookmarks = preferences.getListString("bookmarkeddorms");
            int originalSize = bookmarks.size();

            bookmarkedDorms.clear();
            for (int i = 0; i < bookmarks.size(); i++) {
                String bookmark = bookmarks.get(i);
                // Check if our bookmark is a valid dorm.
                // Note: We do this because update 1.12 has new names for dorms due to new API
                if (DormInformation.getInstance().getIDs().contains(bookmark)) {
                    Dorm d = new Dorm();
                    d.setName(bookmark);
                    d.setID(DormInformation.getInstance().getIDs().get(bookmark));
                    bookmarkedDorms.add(d);
                } else {
                    // Remove invalid bookmarks
                    bookmarks.remove(bookmark);
                    i--;
                }
            }

            if (bookmarks.size() != originalSize) {
                preferences.putListString("bookmarkeddorms", bookmarks);
            }

            return bookmarkedDorms;
        }

        @Override
        protected void onPostExecute(ArrayList<Dorm> dorms) {
            if (dorms != null) {
                mAdapter.setItems(dorms);
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Sorry. Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
            }

            if (bookmarkedDorms.isEmpty()) {
                bgImage.setVisibility(View.VISIBLE);
            } else {
                bgImage.setVisibility(View.INVISIBLE);
            }

            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
