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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocationFragment extends CustomFragment implements View.OnClickListener, OnMapReadyCallback {


    private MainActivity ma;
    private static LocationFragment instance;
    private static String TAG = "LocationFragment";
    private Location location;

    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_view, container, false);
        ma = (MainActivity) getActivity();

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            Log.i(TAG, "MAPFRAGMENT IS NULL");
        } else {
            location = ma.getLastLocation();
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
        if(location == null) {
            Toast.makeText(ma, "GPS not enabled", Toast.LENGTH_LONG).show();
        } else {
            map.setMyLocationEnabled(true);
            LatLng YOU = new LatLng(location.getLatitude(), location.getLongitude());

            map.addMarker(new MarkerOptions().position(YOU).icon(BitmapDescriptorFactory.fromResource(R.drawable.here))
                    .title("HEYHEY!")
                    .snippet("Du er her"));

//            map.addMarker(new MarkerOptions().position(YOU)
//                    .title("HEYHEY!")
//                    .snippet("Du er her"));
            //Move the camera instantly to you with a zoom of 15.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(YOU, 10));
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
        }
    }

}
