package com.ohnana.tipflip.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by jakobgaardandersen on 06/05/15.
 */
@Parcel
public class Store {
    String _id, name, image;

    @ParcelConstructor
    public Store(String _id, String name, String image) {
        this._id = _id;
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    public String getImage() {
        return image;
    }
}
