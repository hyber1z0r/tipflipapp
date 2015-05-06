package com.ohnana.tipflip.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ohnana.tipflip.MainActivity;
import com.ohnana.tipflip.model.Offer;
import com.ohnana.tipflip.adapters.OfferListAdapter;
import com.ohnana.tipflip.R;
import com.ohnana.tipflip.interfaces.TipFlipService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends CustomFragment implements View.OnClickListener {

    private MainActivity ma;
    private static OffersFragment instance;
    public static String TAG = "OFFERSFRAGMENT";
    private TipFlipService service;
    private List<Offer> offers;
    private ListView mListView;
    private List<Offer> mAdapterItems = new ArrayList<>();
    private OfferListAdapter myAdapter;


    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.offers_view, container, false);
        int[] clickButtons = new int[]{ // buttons
        };
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }
        ma = (MainActivity) getActivity();
        init(rootView);
        return rootView;
    }

    private void init(View rootView)
    {
        mListView = (ListView) rootView.findViewById(R.id.listViewOffer);
        myAdapter = new OfferListAdapter(ma, mAdapterItems);
        mListView.setAdapter(myAdapter);

        mAdapterItems = Parcels.unwrap(getArguments().getParcelable("offers"));
        myAdapter.updateData(mAdapterItems);
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
