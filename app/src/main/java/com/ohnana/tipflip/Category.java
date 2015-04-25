package com.ohnana.tipflip;

/**
 * Created by jakobgaardandersen on 25/04/15.
 */
public class Category {

    private String _id, category;

    public Category(String _id, String category){
        this._id = _id;
        this.category = category;
    }

    public String get_id() {
        return _id;
    }

    public String getCategory() {
        return category;
    }

}
