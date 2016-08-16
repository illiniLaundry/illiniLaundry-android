package io.ericlee.illinilaundry.Model;

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

import io.ericlee.illinilaundry.Adapters.AlarmAdapter;

/**
 * Created by Eric on 8/15/2016.
 */

public class MachineParser extends AsyncTask<Void, Void, Boolean> {
    private Dorm mDorm;
    private Machine machine;
    private String timeRemaining;
    private TextView tvTimeRemaining;
    private String updatedAvailability;
    private AlarmAdapter mAdapter;

    public MachineParser(Dorm dorm, Machine machine) {
        mDorm = dorm;
        this.machine = machine;
    }

    public MachineParser(Dorm dorm, Machine machine, AlarmAdapter adapter) {
        mDorm = dorm;
        this.machine = machine;
        mAdapter = adapter;
    }

    public void update(TextView timeRemaining) {
        tvTimeRemaining = timeRemaining;
        execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Document illini = Jsoup.connect(mDorm.getPageUrl()).get();

            // Parse general information
            Element table = illini.select("tbody").last();
            Elements rows = table.select("tr");

            String machineNumber;
            try {
                machineNumber = machine.getMachineNumber().replaceAll("\\D+", "");
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
            machine.setMachineStatus(updatedAvailability);
            machine.setMachineTimeRemaining(timeRemaining);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        try {
            mAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            // All good
        }

        try {
            if (!result) {
                Toast.makeText(tvTimeRemaining.getContext(),
                        "I'm sorry, Dave, I'm afraid I can't do that.", Toast.LENGTH_SHORT).show();
            } else if (updatedAvailability.contains("Available")) {
                //TODO: Handle removing of the alarm
            } else {
                tvTimeRemaining.setText(timeRemaining);
            }
        } catch (NullPointerException e) {
            // All good
        }
    }
}
