package com.ohnana.tipflip;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by jakobgaardandersen on 06/05/15.
 */
@Parcel
public class Store {
    String _id, name;

    @ParcelConstructor
    public Store(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }
}
