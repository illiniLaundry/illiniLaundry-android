package io.ericlee.illinilaundry.Model;

/**
 * Created by Eric on 5/27/2016.
 */
public class Dorm {
    private String name;
    private int wash;
    private int dry;
    private int inWash;
    private int inDry;
    private String pageUrl;

    public Dorm(String name, String pageUrl, int wash, int dry, int inWash, int inDry) {
        this.name = name;
        this.wash = wash;
        this.dry = dry;
        this.inWash = inWash;
        this.inDry = inDry;
        this.pageUrl = pageUrl;
    }

    public String getName() { return name; }
    public int getWash() { return wash; }
    public int getDry() { return dry; }
    public int getInWash() { return inWash; }
    public int getInDry() {return inDry; }
    public String getPageUrl() {return pageUrl; }
}
