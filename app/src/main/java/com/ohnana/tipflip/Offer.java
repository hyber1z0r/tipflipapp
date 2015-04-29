package com.ohnana.tipflip;

/**
 * Created by Messikrkic on 28-04-2015.
 */
public class Offer {

    private String _id;
    private Category category;
    private String discount;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Offer(String _id, Category category, String discount){
        this._id = _id;
        this.category = category;
        this.discount = discount;

    }



}