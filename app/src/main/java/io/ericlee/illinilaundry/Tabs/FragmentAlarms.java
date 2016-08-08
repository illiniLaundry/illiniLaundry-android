package io.ericlee.illinilaundry.Tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import io.ericlee.illinilaundry.Adapters.AlarmAdapter;
import io.ericlee.illinilaundry.Adapters.BookmarkAdapter;
import io.ericlee.illinilaundry.Model.Alarm;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.ItemOffsetDecoration;
import io.ericlee.illinilaundry.Model.TinyDB;
import io.ericlee.illinilaundry.R;

/**
 * Created by Eric on 7/6/2016.
 */
public class FragmentAlarms extends Fragment {
    private TinyDB preferences;
    private ArrayList<Alarm> mAlarms;
    private RecyclerView mRecyclerView;
    private AlarmAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        preferences = TinyDB.getInstance(getContext());
        mAlarms = new ArrayList<>();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mAlarms.isEmpty()) {
            // Delay to allow everything to settle before running SetData()
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new FragmentAlarms.SetData().execute();
                }
            }, 500);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.alarmSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.alarmsRecyclerView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new FragmentAlarms.SetData().execute();
            }
        });

        GridLayoutManager glm = new GridLayoutManager(this.getContext(), 2);
        mRecyclerView.setLayoutManager(glm);
        mAdapter = new AlarmAdapter(mAlarms);

        // Add spacing between cards.
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        // Extend the Callback class
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                ArrayList<Object> forSwappingPreferences = preferences.getListObject("alarms", Alarm.class);

                // Swap positions
                Collections.swap(mAlarms, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Collections.swap(forSwappingPreferences, viewHolder.getAdapterPosition(), target.getAdapterPosition());

                preferences.putListObject("alarms", forSwappingPreferences);

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

    public class SetData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("AlarmSetData", "RUN!");
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

            ArrayList<Object> temp = preferences.getListObject("alarms", Alarm.class);
            mAlarms.clear();

            for(Object o : temp) {
                mAlarms.add((Alarm)o);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();

            ImageView bgImage = (ImageView) getView().findViewById(R.id.backgroundIlliniAlarms);

            if (mAlarms.isEmpty()) {
                bgImage.setVisibility(View.VISIBLE);
            } else {
                bgImage.setVisibility(View.INVISIBLE);
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
