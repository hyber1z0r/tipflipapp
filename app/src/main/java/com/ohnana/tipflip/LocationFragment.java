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

import com.google.android.gms.maps.MapView;
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
    private MapView mMapView;
    private Bundle mBundle;
    private GoogleMap mMap;

    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_view, container, false);
        ma = (MainActivity) getActivity();
        location = ma.getLastLocation();
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(mBundle);
        setUpMapIfNeeded();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
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
        mMap = map;
        setUpMap();
    }

    private void setUpMap() {
        if (location == null) {
            Toast.makeText(ma, "GPS not enabled", Toast.LENGTH_LONG).show();
        } else {
            mMap.setMyLocationEnabled(true);
            LatLng YOU = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(YOU).icon(BitmapDescriptorFactory.fromResource(R.drawable.yourarehere))
                    .title("Du er her"));

            //Move the camera instantly to you with a zoom of 15.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(YOU, 18));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
}
