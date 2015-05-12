package com.ohnana.tipflip.interfaces;

import com.ohnana.tipflip.model.Offer;
import com.ohnana.tipflip.model.Profile;
import com.ohnana.tipflip.model.Category;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * Created by jakobgaardandersen on 24/04/15.
 */
public interface TipFlipService {
    @GET("/profile")
    void getProfile(@Query("regid") String regID, Callback<Profile> cb);

    @GET("/categories")
    void getCategories(Callback<List<Category>> cb);

    @POST("/savereg")
    void saveRegId(@Body HashMap<String, String> body, Callback<JSONObject> cb);

    @GET("/offers")
    void getAllOffers(Callback<List<Offer>> cb);

    @PUT("/profile")
    void updateProfile(@Body Profile body, Callback<JSONObject> cb);
}
