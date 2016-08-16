package io.ericlee.illinilaundry.Model;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Eric on 8/15/2016.
 */

public class DormParser {
    private static DormParser instance;
    private ArrayList<Machine> mMachines;

    private DormParser() {
        mMachines = new ArrayList<>();
    }

    public static DormParser getInstance() {
        if(instance == null) {
            instance = new DormParser();
        }

        return instance;
    }

    public class Parser extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
