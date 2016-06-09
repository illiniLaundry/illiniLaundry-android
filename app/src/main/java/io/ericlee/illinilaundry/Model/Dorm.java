package io.ericlee.illinilaundry.Model;

import android.widget.ImageView;

/**
 * Created by Eric on 5/27/2016.
 */
public class Dorm {
    private String name;
    private int available;
    private int inUse;
    private int inWash;
    private int inDry;

    public Dorm(String name, int available, int inUse, int inWash, int inDry) {
        this.name = name;
        this.available = available;
        this.inUse = inUse;
        this.inWash = inWash;
        this.inDry = inDry;
    }

    public String getName() { return name; }
    public int getAvailable() { return available; }
    public int getInUse() { return inUse; }
    public int getInWash() { return inWash; }
    public int getInDry() {return inDry; }
}
