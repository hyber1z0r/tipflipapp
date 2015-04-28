package com.ohnana.tipflip;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocationFragment extends CustomFragment implements View.OnClickListener, OnMapReadyCallback {


    private MainActivity ma;
    private static LocationFragment instance;
    private static String TAG = "LocationFragment";


    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_view, container, false);
        ma = (MainActivity) getActivity();

        SupportMapFragment mapFragment = (SupportMapFragment) ma.getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            Log.i(TAG, "MAPFRAGMENT IS NULL");
        } else {
            mapFragment.getMapAsync(this);
        }

        return rootView;
    }


    public LocationFragment() {
    }

    public static LocationFragment getInstance() {
        if (instance == null) {
            instance = new LocationFragment();
        }
        return instance;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
/*        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));*/

        Location location = ma.getLocation();
        if(location == null) {
            Toast.makeText(ma, "GPS not enabled", Toast.LENGTH_LONG).show();
        } else {
            LatLng YOU = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions().position(YOU).title("Du er her"));
            //Move the camera instantly to hamburg with a zoom of 15.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(YOU, 15));
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
    }
}
