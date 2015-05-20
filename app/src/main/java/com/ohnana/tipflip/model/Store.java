package com.ohnana.tipflip.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by jakobgaardandersen on 06/05/15.
 */
@Parcel
public class Store implements ClusterItem {
    String _id, name, image;
    String location;
    double latitude, longitude;

    @ParcelConstructor
    public Store(String _id, String name, String image, String location) {
        this._id = _id;
        this.name = name;
        this.image = image;
        this.location = location;
        String[] position = location.split(",");
        this.latitude = Double.parseDouble(position[0]);
        this.longitude = Double.parseDouble(position[1]);
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }
}
