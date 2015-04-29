package com.ohnana.tipflip;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends CustomFragment {

    private MainActivity ma;
    private static HomeFragment instance;
    public static String TAG = "HOMEFRAGMENT";
    private RecyclerView mRecycleView;

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
        List<Offer> offers = new ArrayList<>();
        offers.add(new Offer("1", new Category("a", "parfume", "asd"), "20%"));
        offers.add(new Offer("2", new Category("b", "food", "asd"), "10%"));
        offers.add(new Offer("3", new Category("c", "men", "asd"), "5%"));
        offers.add(new Offer("4", new Category("d", "tøj", "asd"), "25%"));
        offers.add(new Offer("5", new Category("e", "pizza", "asd"), "40%"));
        offers.add(new Offer("6", new Category("f", "sko", "asd"), "100%"));
        offers.add(new Offer("7", new Category("g", "kosttilskud", "asd"), "90%"));
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
