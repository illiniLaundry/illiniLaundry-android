package io.ericlee.illinilaundry.Model;

import android.os.AsyncTask;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Activities.MainActivity;

/**
 * Created by Eric on 5/27/2016.
 */
public class FetchData extends AsyncTask<Void, Void, ArrayList<ArrayList<String>>> {
    private static ArrayList<ArrayList<String>> stuff;

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(Void... params) {
        String url = "https://www.laundryalert.com/cgi-bin/urba7723/LMPage?Login=True";

        stuff = new ArrayList<>();
        try {
            Document illini = Jsoup.connect(url).get();

            Element table = illini.select("tbody").get(2);
            Elements rows = table.select("tr");

            rows.remove(0);
            rows.remove(0);

            for(int i = 0; i < rows.size() - 1; i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                ArrayList<String> temp = new ArrayList<String>();

                for(int j = 0; j < cols.size(); j++) {
                    if(j == 1) { temp.add(cols.get(j).text()); }
                    if(j == 2) { temp.add(cols.get(j).text()); }
                    if(j == 3) { temp.add(cols.get(j).text()); }
                    if(j == 5) { temp.add(cols.get(j).text()); }
                    if(j == 7) { temp.add(cols.get(j).text()); }
                }
                stuff.add(temp);
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(MainActivity.getContext(), "Error!", Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }
        return stuff;
    }
}
