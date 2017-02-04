package io.ericlee.illinilaundry.Model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Parses Dorms from JSON.
 *
 * @author dl-eric
 */

public class MachineParser {
    private final String JSON_URL = "http://23.23.147.128/homes/mydata/urba7723";

    private static MachineParser instance;

    public static MachineParser getInstance() {
        if (instance == null) {
            instance = new MachineParser();
        }

        return instance;
    }

    public Machine[] getData() throws IOException {
        JsonParser jp = new JsonParser();
        JsonElement json = jp.parse(getJSON());
        JsonElement jsonDorms = json.getAsJsonObject().getAsJsonArray("rooms");

        Type listType = new TypeToken<Machine[]>() {}.getType();

        return new Gson().fromJson(jsonDorms, listType);
    }

    private String getJSON() throws IOException {
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
