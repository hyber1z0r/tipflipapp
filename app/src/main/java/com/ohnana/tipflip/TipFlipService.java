package com.ohnana.tipflip;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by jakobgaardandersen on 24/04/15.
 */
public interface TipFlipService {
    @GET("/profile")
    void getProfile(@Query("name") String name, Callback<User> cb);

    @GET("/categories")
    void getCategories(Callback<List<Category>> cb);

    @POST("/savereg")
    void saveRegId(@Body HashMap<String, String> body, Callback<JSONObject> cb);
}
