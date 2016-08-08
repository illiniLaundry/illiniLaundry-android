package io.ericlee.illinilaundry.Model;

import java.io.Serializable;

/**
 * Created by Eric on 5/27/2016.
 */
public class Dorm implements Serializable {
    private String name;
    private int wash;
    private int dry;
    private int inWash;
    private int inDry;
    private String pageUrl;
    private int imageResource;

    public Dorm(String name, String pageUrl, int wash, int dry, int inWash, int inDry) {
        this.name = name;
        this.wash = wash;
        this.dry = dry;
        this.inWash = inWash;
        this.inDry = inDry;
        this.pageUrl = pageUrl;
        imageResource = DormImages.getInstance().getImages().get(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWash() {
        return wash;
    }

    public void setWash(int wash) {
        this.wash = wash;
    }

    public int getDry() {
        return dry;
    }

    public void setDry(int dry) {
        this.dry = dry;
    }

    public int getInWash() {
        return inWash;
    }

    public void setInWash(int inWash) {
        this.inWash = inWash;
    }

    public int getInDry() {
        return inDry;
    }

    public void setInDry(int inDry) {
        this.inDry = inDry;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public int getImageResource() { return imageResource; }
}
