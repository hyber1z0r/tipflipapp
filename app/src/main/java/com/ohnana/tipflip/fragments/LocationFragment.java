package com.ohnana.tipflip.fragments;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.ohnana.tipflip.MainActivity;
import com.ohnana.tipflip.R;
import com.ohnana.tipflip.model.Store;

import java.util.ArrayList;
import java.util.List;


public class LocationFragment extends CustomFragment implements View.OnClickListener, OnMapReadyCallback {


    private MainActivity ma;
    private static LocationFragment instance;
    private static String TAG = "LocationFragment";
    private Location location;
    private Marker myMarker, lyngbyMarker;


    // Declare a variable for the cluster manager.
    private ClusterManager<Store> mClusterManager;

    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_view, container, false);
        ma = (MainActivity) getActivity();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
        if (location == null) {
            Toast.makeText(ma, "GPS not enabled", Toast.LENGTH_LONG).show();
        } else {
            // Initialize the manager with the context and the map.
            // (Activity extends context, so we can pass 'this' in the constructor.)
            mClusterManager = new ClusterManager<Store>(ma, map);

            // Point the map's listeners at the listeners implemented by the cluster
            // manager.
            map.setOnCameraChangeListener(mClusterManager);
            map.setOnMarkerClickListener(mClusterManager);

            // Add cluster items (markers) to the cluster manager.
            addItems();

         // stores =  map.addMarker(new MarkerOptions().position(STORES.position).icon(BitmapDescriptorFactory.fromResource(R.drawable.dollar_sign))
         // .title(STORES.name)
         // .snippet(STORES.description));


            map.setMyLocationEnabled(true);
            LatLng YOU = new LatLng(location.getLatitude(), location.getLongitude());

            LatLng LyngbyStorcenter = new LatLng(55.772002,12.50649);

            myMarker = map.addMarker(new MarkerOptions().position(YOU).icon(BitmapDescriptorFactory.fromResource(R.drawable.here1))
                    .title("HEYHEY!")
                    .snippet("Du er her"));

            lyngbyMarker = map.addMarker(new MarkerOptions().position(LyngbyStorcenter).icon(BitmapDescriptorFactory.fromResource(R.drawable.shoppingicon))
                    .title("Lyngby Storcenter"));


            myMarker.showInfoWindow();
            lyngbyMarker.showInfoWindow();
//            map.addMarker(new MarkerOptions().position(YOU)
//                    .title("HEYHEY!")
//                    .snippet("Du er her"));
            //Move the camera instantly to you with a zoom of 15.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(YOU, 10));
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
            map.getUiSettings().setMyLocationButtonEnabled(true);

        }
    }

    private void addItems() {
        List<Store> stores = new ArrayList<>();


        stores.add(new Store("1", "Vuks house", "", "55.7375619,12.4559613"));
        stores.add(new Store("2", "Jakobs house", "", "55.742937,12.511377"));
        stores.add(new Store("3", "Damjans flat", "", "55.9362936,12.3164437"));
        stores.add(new Store("4", "Fisketorvet", "", "55.6646305,12.5657289"));
        stores.add(new Store("5", "Fisketorvet", "", "55.6649512,12.566716"));
        stores.add(new Store("6", "Fisketorvet", "", "55.6654656,12.5657825"));
        stores.add(new Store("7", "Fisketorvet", "", "55.6654958,12.5662975"));
        stores.add(new Store("8", "Fisketorvet", "", "55.66457,12.5650208"));
        stores.add(new Store("9", "Fisketorvet", "", "55.6646729,12.5643127"));
        stores.add(new Store("10", "Fisketorvet", "", "55.6643098,12.5650315"));
        stores.add(new Store("11", "Fisketorvet", "", "55.6640193,12.5659435"));
        stores.add(new Store("12", "Fisketorvet", "", "55.6643582,12.5664585"));
        stores.add(new Store("13", "Fisketorvet", "", "55.6647515,12.5669413"));


        mClusterManager.addItems(stores);

        }






}
