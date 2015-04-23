package com.ohnana.tipflip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SubscribeFragment extends CustomFragment implements View.OnClickListener {

    private MainActivity ma;
    private static SubscribeFragment instance;
    public static String TAG = "SUBSCRIBEFRAGMENT";

    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscribe_view, container, false);
        int[] clickButtons = new int[]{ // buttons
        };
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }
        ma = (MainActivity) getActivity();
        return rootView;
    }


    public SubscribeFragment() {
    }

    public static SubscribeFragment getInstance() {
        if (instance == null) {
            instance = new SubscribeFragment();
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
