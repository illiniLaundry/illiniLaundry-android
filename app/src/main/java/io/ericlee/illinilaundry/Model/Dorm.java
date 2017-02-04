package io.ericlee.illinilaundry.Model;

/**
 * Dorm object for GSON
 *
 * @author dl-eric
 */
public class Dorm {
    private String name;
    private Machine[] machines;
    private int imageResource;

    public Dorm() {
        imageResource = DormImages.getInstance().getImages().get(name);
    }

    public int getImageResource() { return imageResource; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Machine[] getMachines() { return machines; }

    public void setMachines(Machine[] machines) { this.machines = machines;}

}
