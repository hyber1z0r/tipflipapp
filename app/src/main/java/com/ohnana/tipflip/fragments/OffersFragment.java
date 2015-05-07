package com.ohnana.tipflip.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ohnana.tipflip.MainActivity;
import com.ohnana.tipflip.adapters.HomeRVAdapter;
import com.ohnana.tipflip.model.Offer;
import com.ohnana.tipflip.R;
import com.ohnana.tipflip.interfaces.TipFlipService;

import org.parceler.Parcels;

import java.util.List;

public class OffersFragment extends CustomFragment implements View.OnClickListener {

    private MainActivity ma;
    private static OffersFragment instance;
    public static String TAG = "OFFERSFRAGMENT";
    private TipFlipService service;
    private List<Offer> offers;
    private RecyclerView mRecycleView;


    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.offers_view, container, false);
        ma = (MainActivity) getActivity();
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.offer_recview);
        LinearLayoutManager llm = new LinearLayoutManager(ma);
        mRecycleView.setLayoutManager(llm);
        this.offers = Parcels.unwrap(getArguments().getParcelable("offers"));
        HomeRVAdapter mAdapter = new HomeRVAdapter(offers);
        mRecycleView.setAdapter(mAdapter);
        return rootView;
    }

    public OffersFragment() {
    }

    public static OffersFragment getInstance() {
        if (instance == null) {
            instance = new OffersFragment();
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
