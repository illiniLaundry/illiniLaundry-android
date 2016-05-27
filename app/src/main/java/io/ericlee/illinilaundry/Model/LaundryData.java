package io.ericlee.illinilaundry.Model;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import io.ericlee.illinilaundry.Activities.MainActivity;

/**
 * Created by Eric on 5/27/2016.
 */
public class LaundryData {
    private static LaundryData instance = null;

    private static ArrayList<ArrayList<String>> stuff;

    private LaundryData() {
        try {
            stuff = new FetchData().execute().get();
            Log.i("hello", stuff.get(0).get(2));
        } catch (InterruptedException e) {
            Log.i("hello", "no");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.i("hello", "no");
            e.printStackTrace();
        }
    }

    public static LaundryData getInstance() {
        if(instance == null) {
            stuff = new ArrayList<>();
            instance = new LaundryData();
        }

        return instance;
    }

    public ArrayList<ArrayList<String>> getData() {
        return stuff;
    }
}
