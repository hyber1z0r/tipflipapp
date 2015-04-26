package com.ohnana.tipflip;

import java.util.List;

/**
 * Created by jakobgaardandersen on 24/04/15.
 */
public class User {
    private String name, _id, regID;
    private List<String> offers;
    private List<Category> categories;
    // for now its string, but should be category objects

    public User(String _id, String name, String regID, List<String> offers, List<Category> categories) {
        this._id = _id;
        this.name = name;
        this.regID = regID;
        this.offers = offers;
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<String> getOffers() {
        return offers;
    }

    public String getName() {
        return name;
    }

    public String getRegID() {
        return regID;
    }

    public String get_id() {
        return _id;
    }
}
