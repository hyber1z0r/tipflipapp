package com.ohnana.tipflip;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends CustomFragment {

    private MainActivity ma;
    private static HomeFragment instance;
    public static String TAG = "HOMEFRAGMENT";
    private RecyclerView mRecycleView;
    private Profile profile;

    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_view, container, false);
        ma = (MainActivity) getActivity();
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.home_recview);
        LinearLayoutManager llm = new LinearLayoutManager(ma);
        mRecycleView.setLayoutManager(llm);
        this.profile = Parcels.unwrap(getArguments().getParcelable("profile"));
        List<Offer> offers = profile.getOffers();
        HomeRVAdapter mAdapter = new HomeRVAdapter(offers);
        mRecycleView.setAdapter(mAdapter);
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

}
