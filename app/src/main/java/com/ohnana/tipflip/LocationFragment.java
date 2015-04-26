package com.ohnana.tipflip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class LocationFragment extends CustomFragment implements View.OnClickListener {


    private MainActivity ma;
    private static LocationFragment instance;
    public static String TAG = "LOCATIONFRAGMENT";
    private static View view;


    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        view = inflater.inflate(R.layout.location_view, container, false);
        int[] clickButtons = new int[]{ // buttons
        };
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }
        ma = (MainActivity) getActivity();

       // Location location = ma.displayLocation();
       // LatLng YOU = new LatLng(location.getLatitude(), location.getLongitude());
       //   Marker youarehere = map.addMarker(new MarkerOptions().position(YOU)
          //        .title("Du er her"));
        // Move the camera instantly to hamburg with a zoom of 15.
        //     map.moveCamera(CameraUpdateFactory.newLatLngZoom(YOU, 15));
        // Zoom in, animating the camera.
        //      map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);



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
}
