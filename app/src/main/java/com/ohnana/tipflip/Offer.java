package com.ohnana.tipflip;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

/**
 * Created by Messikrkic on 28-04-2015.
 */
@Parcel
public class Offer {

    Category category;
    String _id, discount, offer, image;
    Date created, expiration;
    Store store;

    @ParcelConstructor
    public Offer(String _id, Category category, String discount, String offer,
                 String image, Date created, Date expiration, Store store) {
        this._id = _id;
        this.category = category;
        this.discount = discount;
        this.offer = offer;
        this.image = image;
        this.created = created;
        this.expiration = expiration;
        this.store = store;
    }

    public String getOffer() {
        return offer;
    }

    public String getImage() {
        return image;
    }

    public Date getCreated() {
        return created;
    }

    public Date getExpiration() {
        return expiration;
    }

    public Store getStore() {
        return store;
    }

    public String get_id() {
        return _id;
    }

    public Category getCategory() {
        return category;
    }

    public String getDiscount() {
        return discount;
    }
}