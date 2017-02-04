package io.ericlee.illinilaundry.Model;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Parses Dorms from JSON.
 *
 * @author dl-eric
 */

public class DormParser {
    private final String JSON_URL = "http://23.23.147.128/homes/mydata/urba7723";

    private static DormParser instance;

    public static DormParser getInstance() {
        if (instance == null) {
            instance = new DormParser();
        }

        return instance;
    }

    public ArrayList<Dorm> getData() throws IOException {
        JsonArray jsonRooms = new JsonParser().parse(getJSON())
                .getAsJsonObject().getAsJsonObject("location")
                .getAsJsonArray("rooms");

        Type type = new TypeToken<ArrayList<Dorm>>(){}.getType();
        ArrayList<Dorm> parsedDorms = new Gson().fromJson(jsonRooms, type);
        return parsedDorms;
    }

    private String getJSON() throws IOException, NetworkOnMainThreadException {
        URL api = new URL(JSON_URL);;
        StringBuffer jsonResponse = new StringBuffer();

        HttpURLConnection con = (HttpURLConnection) api.openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));

        String input = in.readLine();

        while (input != null) {
            jsonResponse.append(input);
            input = in.readLine();
        }

        in.close();

        return jsonResponse.toString();
    }
}
