package io.ericlee.illinilaundry.Model;

import java.util.Dictionary;
import java.util.Hashtable;

import io.ericlee.illinilaundry.R;

public class DormImages {

    public static DormImages instance;

    private Dictionary<String, Integer> images;

    private DormImages() {
        images = new Hashtable<>(30);

        // TODO: Get all the dorm pictures!

        images.put("Allen", R.drawable.allen);
        images.put("Barton-Lundgren", R.drawable.bartonlundgren);
        images.put("Bousfield Rm 103", R.drawable.bousfield);
        images.put("Busey-Evans", R.drawable.buseyevans);
        images.put("Daniels North", R.drawable.daniels);
        images.put("Daniels South", R.drawable.daniels);
        images.put("FAR: Oglesby", R.drawable.far);
        images.put("FAR: Trelease", R.drawable.far);
        images.put("300 South Goodwin", 0);
        images.put("1107 West Green", 0);
        images.put("Hopkins", 0);
        images.put("ISR: Townsend", R.drawable.isrtownsend);
        images.put("ISR: Wardall", R.drawable.isr);
        images.put("LAR: Leonard", R.drawable.lar);
        images.put("LAR: Shelden", R.drawable.lar);
        images.put("Nugent", 0);
        images.put("Nugent Rm 126", 0);
        images.put("Orchard Downs North", 0);
        images.put("Orchard Downs South", 0);
        images.put("PAR: Babcock", R.drawable.par);
        images.put("PAR: Blaisdell", R.drawable.par);
        images.put("PAR: Carr", R.drawable.par);
        images.put("PAR: Saunders", R.drawable.par);
        images.put("Scott", 0);
        images.put("Sherman Short", 0);
        images.put("Sherman Tall", 0);
        images.put("Snyder", 0);
        images.put("TVD: Taft", 0);
        images.put("TVD: Van Doren", 0);
        images.put("Weston", 0);
    }

    public static DormImages getInstance() {
        if (instance == null) {
            instance = new DormImages();
        }

        return instance;
    }

    public Dictionary<String, Integer> getImages() { return images; }
}
