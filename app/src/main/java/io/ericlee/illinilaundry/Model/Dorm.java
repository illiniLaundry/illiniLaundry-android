package io.ericlee.illinilaundry.Model;

import android.databinding.BindingAdapter;

/**
 * Dorm object for GSON
 *
 * @author dl-eric
 */
public class Dorm {
    private String name;
    private Machine[] machines;
    private int imageResource;

    public Dorm(String name, Machine[] machines) {
        this.name = name;
        this.machines = machines;
        imageResource = DormImages.getInstance().getImages().get(name);
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Machine[] getMachines() { return machines; }

    public void setMachines(Machine[] machines) { this.machines = machines;}

}
