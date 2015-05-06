package com.ohnana.tipflip.adapters;

/**
 * Created by jakobgaardandersen on 29/04/15.
 */
public class DrawerListItem {
    private String icon, title;

    public DrawerListItem(String icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}
