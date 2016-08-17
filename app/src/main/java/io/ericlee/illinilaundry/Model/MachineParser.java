package io.ericlee.illinilaundry.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import io.ericlee.illinilaundry.Adapters.AlarmAdapter;
import io.ericlee.illinilaundry.R;

/**
 * Created by Eric on 8/15/2016.
 */

public class MachineParser extends AsyncTask<Void, Void, Boolean> {
    private String timeRemaining;
    private String updatedAvailability;
    private AlarmAdapter mAdapter;
    private ArrayList<Alarm> mDataset;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public MachineParser(AlarmAdapter adapter, ArrayList<Alarm> mDataset,
                         Context context) {
        mAdapter = adapter;
        this.mDataset = mDataset;
        mContext = context;
    }

    public void setRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            for (int i = 0; i < mDataset.size(); i++) {
                Alarm mAlarm = mDataset.get(i);
                Document illini = Jsoup.connect(mAlarm.getDorm().getPageUrl()).get();

                // Parse general information
                Element table = illini.select("tbody").last();
                Elements rows = table.select("tr");

                String machineNumber;
                try {
                    machineNumber = mAlarm.getMachine().getMachineNumber().replaceAll("\\D+", "");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }

                //TODO: case where the machine number is e.g L3
                int indexForMachineNumber = Integer.parseInt(machineNumber);
                // check for announcement
                Element firstRow = rows.get(0);
                Elements firstRowCols = firstRow.select("td");

                if (firstRowCols.size() == 3) {
                    indexForMachineNumber++;
                }
                Log.i("Index for machine num", indexForMachineNumber + "");
                Element row = rows.get(indexForMachineNumber);
                Elements cols = row.select("td");

                updatedAvailability = cols.get(4).text();
                Log.i("Updated Availability", updatedAvailability);
                timeRemaining = cols.get(5).text();
                mAlarm.getMachine().setMachineStatus(updatedAvailability);
                mAlarm.getMachine().setMachineTimeRemaining(timeRemaining);

                if (updatedAvailability.contains("Available")) {
                    mDataset.remove(mAlarm);
                }

                ArrayList<Object> newAlarmsAsObjects = new ArrayList<>(mDataset.size());

                for (Alarm a : mDataset) {
                    newAlarmsAsObjects.add(a);
                }

                TinyDB preferences = TinyDB.getInstance(mContext);
                preferences.putListObject("alarms", newAlarmsAsObjects);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        mAdapter.notifyDataSetChanged();

        try {
            if (!result) {
                Toast.makeText(mContext,
                        "I'm sorry, Dave, I'm afraid I can't do that.", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            // All good
        }

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
