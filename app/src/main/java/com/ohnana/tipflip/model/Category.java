package com.ohnana.tipflip.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
/**
 * Created by jakobgaardandersen on 25/04/15.
 */
@Parcel
public class Category {

    String _id, name, image;

    @ParcelConstructor
    public Category(String _id, String name, String image) {
        this._id = _id;
        this.name = name;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

}
