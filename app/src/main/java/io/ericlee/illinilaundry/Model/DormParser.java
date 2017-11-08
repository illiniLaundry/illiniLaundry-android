package io.ericlee.illinilaundry.Model;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;

/**
 * Parses Dorms from JSON.
 *
 * @author dl-eric
 */

public class DormParser {
    private final String URL = "http://api.laundryview.com/room/?api_key=" + API.key;
    private final String[] dormIDs = {
            "589877048", "589877054", "589877017", "589877019", "589877004", "589877022", "589877043", "589877003",
            "589877021", "589877044", "589877027", "589877026", "589877032", "589877030", "589877031", "589877038",
            "589877057", "589877037", "589877036", "589877040", "589877023", "589877046", "589877024", "589877008",
            "589877009", "589877052", "589877051", "589877015", "589877002", "589877058", "589877061", "589877028"
    };

    private static DormParser instance;

    public static DormParser getInstance() {
        if (instance == null) {
            instance = new DormParser();
        }

        return instance;
    }

    public Dorm getDormData(String id) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(getXML(id)));

        Dorm dorm = new Dorm();
        dorm.setID((id));

        ArrayList<Machine> machines = new ArrayList<>();
        Machine machine = null;

        boolean add = true;
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tag;
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tag = xpp.getName();
                    if (tag.equals("laundry_room_name")) {
                        dorm.setName(xpp.nextText());
                    }

                    else if (tag.equals("appliance")) {
                        machine = new Machine();
                    }

                    else if (tag.equals("appliance_type")) {
                        machine.setType(xpp.nextText());
                    }

                    else if (tag.equals("status")) {
                        machine.setStatus(xpp.nextText());
                    }

                    else if (tag.equals("out_of_service")) {
                        String bool = xpp.nextText();
                        if (bool.equals("0")) {
                            machine.setIsOutOfService(false);
                        } else {
                            machine.setIsOutOfService(true);
                        }
                    }

                    else if (tag.equals("label")) {
                        machine.setLabel(xpp.nextText());

                        // The API sometimes returns a bad machine, which is indicated by an empty label.
                        if (machine.getLabel().isEmpty()) {
                            add = false;
                        }   else {
                            add = true;
                        }
                    }

                    else if (tag.equals("time_remaining")) {
                        machine.setTimeRemaining(xpp.nextText());
                    }

                    break;
                case XmlPullParser.END_TAG:
                    tag = xpp.getName();
                    if (tag.equals("appliance") && add) {
                        machines.add(machine);
                    }

                    break;
                default:
                    break;
            }

            eventType = xpp.next();
        }

        dorm.setMachines(machines.toArray(new Machine[machines.size()]));

        return dorm;
    }

    public ArrayList<Dorm> getData() throws Exception {
        ArrayList<Dorm> dorms = new ArrayList<>();

        // Go through each dorm ID and create a Dorm object from the XML
        // Note: The dorm that is created does not have machine[] info
        // Additional Note: We only do this because we do not have an API endpoint (yet) to fetch
        // the school's dorms. This method is fast.
        Enumeration<String> keys = DormInformation.getInstance().getIDs().keys();
        while (keys.hasMoreElements()) {
            String dormName = keys.nextElement();
            String dormID = DormInformation.getInstance().getIDs().get(dormName);

            Dorm dorm = new Dorm();
            dorm.setName(dormName);
            dorm.setID(dormID);
            dorms.add(dorm);
        }

        Collections.sort(dorms, new Comparator<Dorm>() {
            @Override
            public int compare(Dorm o1, Dorm o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return dorms;
    }

    private String getXML(String dormID) throws IOException, NetworkOnMainThreadException {
        URL api = new URL(URL + "&method=getAppliances&location=" + dormID);
        StringBuffer xmlResponse = new StringBuffer();

        HttpURLConnection con = (HttpURLConnection) api.openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));

        String input = in.readLine();

        while (input != null) {
            xmlResponse.append(input);
            input = in.readLine();
        }

        in.close();

        return xmlResponse.toString();
    }
}
