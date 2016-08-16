package io.ericlee.illinilaundry.Model;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Eric on 8/15/2016.
 */

public class MainParser {
    private static MainParser instance;
    private ArrayList<Dorm> mDorms;
    private String statusAnnouncement;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.Adapter mAdapter;
    private ImageView bgImage;

    public static MainParser getInstance() {
        if (instance == null) {
            instance = new MainParser();
        }

        return instance;
    }

    private MainParser() {
        mDorms = new ArrayList<>(31);
        statusAnnouncement = "";
    }

    public void parse(SwipeRefreshLayout swipeRefreshLayout, RecyclerView.Adapter mAdapter,
                      ImageView bgImage) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        this.mAdapter = mAdapter;
        this.bgImage = bgImage;
        new Parser().execute();
    }

    public ArrayList<Dorm> getDorms() {
        return mDorms;
    }

    public String getStatusAnnouncement() {
        return statusAnnouncement;
    }

    public String toString() {
        return "";
    }

    public class Parser extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String url = "https://www.laundryalert.com/cgi-bin/urba7723/LMPage?Login=True";
                Document illini = Jsoup.connect(url).get();

                Element table = illini.select("tbody").get(2);
                Elements rows = table.select("tr");

                int i = 1;

                // Check for announcement
                Element firstRow = rows.get(0);
                Elements firstRowCols = firstRow.select("td");

                if (firstRowCols.size() == 2) {
                    statusAnnouncement = firstRowCols.get(1).text();
                    Log.i("announcement", statusAnnouncement);
                    i++;
                }

                if (mDorms.isEmpty()) {
                    for (; i < rows.size() - 1; i++) {
                        Element row = rows.get(i);
                        Elements cols = row.select("td");

                        try {
                            mDorms.add(new Dorm(
                                    cols.get(1).text(),                                   // Name
                                    "https://www.laundryalert.com/cgi-bin/urba7723/"      // URL
                                            + cols.get(1).attr("onclick").split("href=")[1].replace(
                                            "'", ""),
                                    Integer.parseInt(cols.get(2).text()),                 // Wash
                                    Integer.parseInt(cols.get(3).text()),                 // Dry
                                    Integer.parseInt(cols.get(5).text()),                 // In Wash
                                    Integer.parseInt(cols.get(7).text())                  // In Dry
                            ));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            mDorms.add(new Dorm(
                                    cols.get(1).text(),
                                    "https://www.laundryalert.com/cgi-bin/urba7723/"      // URL
                                            + cols.get(1).attr("onclick").split("href=")[1].replace(
                                            "'", ""),
                                    -1,                                                   // Wash
                                    -1,                                                   // Dry
                                    -1,                                                   // In Wash
                                    -1                                                    // In Dry
                            ));
                        }
                    }
                } else {
                    int j = 0;
                    for (; i < rows.size() - 1; i++) {
                        Dorm tempDorm = mDorms.get(j++);
                        Element row = rows.get(i);
                        Elements cols = row.select("td");

                        tempDorm.setName(cols.get(1).text());
                        tempDorm.setPageUrl("https://www.laundryalert.com/cgi-bin/urba7723/"
                                + cols.get(1).attr("onclick").split("href=")[1].replace("'", ""));

                        try {
                            tempDorm.setWash(Integer.parseInt(cols.get(2).text()));
                            tempDorm.setDry(Integer.parseInt(cols.get(3).text()));
                            tempDorm.setInWash(Integer.parseInt(cols.get(5).text()));
                            tempDorm.setInDry(Integer.parseInt(cols.get(7).text()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            tempDorm.setWash(-1);
                            tempDorm.setDry(-1);
                            tempDorm.setInWash(-1);
                            tempDorm.setInDry(-1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            Log.i("Dorm Dataset", getDorms().size() + "");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {
                Toast.makeText(mSwipeRefreshLayout.getContext(),
                        "Connection error occurred. Try again later.", Toast.LENGTH_LONG).show();
            }

            mAdapter.notifyDataSetChanged();
            if (!statusAnnouncement.equals("")) {
                Toast.makeText(mSwipeRefreshLayout.getContext(), statusAnnouncement,
                        Toast.LENGTH_LONG).show();
            }

            if (mDorms.isEmpty()) {
                bgImage.setVisibility(View.VISIBLE);
            } else {
                bgImage.setVisibility(View.INVISIBLE);
            }

            // Notify swipeRefreshLayout that the refresh has finished
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
