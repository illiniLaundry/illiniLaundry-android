package io.ericlee.illinilaundry.Model;

import android.databinding.BindingAdapter;

import java.io.Serializable;

/**
 * Dorm object for GSON
 *
 * @author dl-eric
 */
public class Dorm implements Serializable {
    private String name;
    private Machine[] machines;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Machine[] getMachines() { return machines; }

    public void setMachines(Machine[] machines) { this.machines = machines;}

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Dorm)) {
            return false;
        }

        return name.equals(((Dorm) obj).getName());
    }
}
