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
        images.put("Barton-Lundgren", 0);
        images.put("Bousfield Rm 103", 0);
        images.put("Busey-Evans", 0);
        images.put("Daniels North", 0);
        images.put("Daniels South", 0);
        images.put("FAR: Oglesby", 0);
        images.put("FAR: Trelease", 0);
        images.put("300 South Goodwin", 0);
        images.put("1107 West Green", 0);
        images.put("Hopkins", 0);
        images.put("ISR: Townsend", 0);
        images.put("ISR: Wardall", R.drawable.isr);
        images.put("LAR: Leonard", 0);
        images.put("LAR: Shelden", 0);
        images.put("Nugent", 0);
        images.put("Nugent Rm 126", 0);
        images.put("Orchard Downs North", 0);
        images.put("Orchard Downs South", 0);
        images.put("PAR: Babcock", 0);
        images.put("PAR: Blaisdell", 0);
        images.put("PAR: Carr", 0);
        images.put("PAR: Saunders", 0);
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
