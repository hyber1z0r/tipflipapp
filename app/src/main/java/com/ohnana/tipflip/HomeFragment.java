package com.ohnana.tipflip;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends CustomFragment implements View.OnClickListener {

    private MainActivity ma;
    private static HomeFragment instance;
    public static String TAG = "HOMEFRAGMENT";

    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_view, container, false);
        int[] clickButtons = new int[]{ R.id.buttonSendPost // buttons
        };
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }
        ma = (MainActivity) getActivity();
        return rootView;
    }


    public HomeFragment() {
    }

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSendPost: //if this button is clicked
                // make that call to webserver
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Log.i(TAG, "onClick do in Background");
                        ma.sendRegistrationIdToBackend();
                        return null;
                    }
                }.execute(null, null, null);
                break;
        }
    }
}
