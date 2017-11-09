package io.ericlee.illinilaundry.Model;

import java.util.Dictionary;
import java.util.Hashtable;

import io.ericlee.illinilaundry.R;

public class DormInformation {

    private static DormInformation instance;

    private Dictionary<String, Integer> images;
    private Dictionary<String, String> ids;

    private DormInformation() {
        images = new Hashtable<>(32);

        images.put("ALLEN, ROOM 49", R.drawable.allen);
        images.put("BOUSFIELD-RM 101 DRYERS", R.drawable.bousfield);
        images.put("BOUSFIELD-RM 103 WASHERS", R.drawable.bousfield);
        images.put("BUSEY EVANS ROOM 8", R.drawable.buseyevans);
        images.put("CLARK HALL, ROOM 30", R.drawable.allen);    // TODO:
        images.put("DANIELS, NORTH ROOM 11", R.drawable.daniels);
        images.put("DANIELS, SOUTH ROOM 40", R.drawable.daniels);
        images.put("FAR, OGLESBY ROOM 1", R.drawable.far);
        images.put("FAR, TRELEASE ROOM 13", R.drawable.far);
        images.put("GOODWIN-GREEN, GOODWIN ROOM 8", R.drawable.lar);        // TODO:
        images.put("GOODWIN GREEN, GREEN ROOM 31", R.drawable.lar);          // TODO:
        images.put("HOPKINS, ROOM 150", R.drawable.hopkins);
        images.put("ISR, TOWNSEND ROOM 35", R.drawable.isrtownsend);
        images.put("ISR, WARDALL ROOM 12", R.drawable.isr);
        images.put("LAR, NORTH ROOM 45", R.drawable.lar);
        images.put("LAR, SOUTH ROOM 29", R.drawable.lar);
        images.put("NUGENT, ROOM 31", R.drawable.nugent);
        images.put("NUGENT, ROOM 35", R.drawable.nugent);
        images.put("NUGENT, ROOM 126", R.drawable.nugent);
        images.put("ORCHARD DOWNS, NORTH LAUNDRY", R.drawable.orcharddownsnorth);
        images.put("PAR, BABCOCK ROOM 23", R.drawable.par);
        images.put("PAR, BLAISDELL ROOM 21B", R.drawable.par);
        images.put("PAR, CARR ROOM 22", R.drawable.par);
        images.put("PAR, SAUNDERS ROOM 23", R.drawable.par);
        images.put("SCOTT, ROOM 170", R.drawable.scott);
        images.put("SHERMAN, 13 STORY ROOM 52", R.drawable.sherman);
        images.put("SHERMAN, 5 STORY ROOM 29", R.drawable.sherman);
        images.put("SNYDER, ROOM 182", R.drawable.snyder);
        images.put("TVD, TAFT ROOM 13", R.drawable.tvd);
        images.put("TVD, VAN DOREN ROOM 13", R.drawable.tvd);
        images.put("WASSAJA, ROOM 1109", R.drawable.wassaja);
        images.put("WESTON, ROOM 100", R.drawable.weston);

        ids = new Hashtable<>(32);

        ids.put("ALLEN, ROOM 49", "589877048");
        ids.put("BOUSFIELD-RM 101 DRYERS", "589877054");
        ids.put("BOUSFIELD-RM 103 WASHERS", "589877017");
        ids.put("BUSEY EVANS ROOM 8", "589877019");
        ids.put("CLARK HALL, ROOM 30", "589877004");
        ids.put("DANIELS, NORTH ROOM 11", "589877022");
        ids.put("DANIELS, SOUTH ROOM 40", "589877043");
        ids.put("FAR, OGLESBY ROOM 1", "589877003");
        ids.put("FAR, TRELEASE ROOM 13", "589877021");
        ids.put("GOODWIN-GREEN, GOODWIN ROOM 8", "589877027");
        ids.put("GOODWIN GREEN, GREEN ROOM 31", "589877044");
        ids.put("HOPKINS, ROOM 150", "589877026");
        ids.put("ISR, TOWNSEND ROOM 35", "589877032");
        ids.put("ISR, WARDALL ROOM 12", "589877030");
        ids.put("LAR, NORTH ROOM 45", "589877031");
        ids.put("LAR, SOUTH ROOM 29", "589877038");
        ids.put("NUGENT, ROOM 31", "589877036");
        ids.put("NUGENT, ROOM 35", "589877037");
        ids.put("NUGENT, ROOM 126", "589877057");
        ids.put("ORCHARD DOWNS, NORTH LAUNDRY", "589877040");
        ids.put("PAR, BABCOCK ROOM 23", "589877023");
        ids.put("PAR, BLAISDELL ROOM 21B", "589877046");
        ids.put("PAR, CARR ROOM 22", "589877024");
        ids.put("PAR, SAUNDERS ROOM 23", "589877008");
        ids.put("SCOTT, ROOM 170", "589877009");
        ids.put("SHERMAN, 13 STORY ROOM 52", "589877052");
        ids.put("SHERMAN, 5 STORY ROOM 29", "589877051");
        ids.put("SNYDER, ROOM 182", "589877015");
        ids.put("TVD, TAFT ROOM 13", "589877002");
        ids.put("TVD, VAN DOREN ROOM 13", "589877058");
        ids.put("WASSAJA, ROOM 1109", "589877061");
        ids.put("WESTON, ROOM 100", "589877028");
    }

    public static DormInformation getInstance() {
        if (instance == null) {
            instance = new DormInformation();
        }

        return instance;
    }

    public Dictionary<String, Integer> getImages() { return images; }
    public Dictionary<String, String> getIDs() { return ids; }
}
