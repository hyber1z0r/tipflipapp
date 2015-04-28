package com.ohnana.tipflip;

/**
 * Created by jakobgaardandersen on 25/04/15.
 */
public class Category {

    private String _id, category, image;

    public Category(String _id, String category, String image) {
        this._id = _id;
        this.category = category;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

}
