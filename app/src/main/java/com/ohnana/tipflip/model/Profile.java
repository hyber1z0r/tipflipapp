package com.ohnana.tipflip.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

/**
 * Created by jakobgaardandersen on 24/04/15.
 */
@Parcel
public class Profile {
    String name, _id, regID;
    List<Offer> offers;
    List<Category> categories;

    @ParcelConstructor
    public Profile(String _id, String name, String regID, List<Offer> offers, List<Category> categories) {
        this._id = _id;
        this.name = name;
        this.regID = regID;
        this.offers = offers;
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Offer> getOffers() {
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
