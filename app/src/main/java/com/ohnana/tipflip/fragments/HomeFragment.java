package com.ohnana.tipflip.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ohnana.tipflip.adapters.HomeRVAdapter;
import com.ohnana.tipflip.MainActivity;
import com.ohnana.tipflip.listeners.RecyclerItemClickListener;
import com.ohnana.tipflip.model.Offer;
import com.ohnana.tipflip.model.Profile;
import com.ohnana.tipflip.R;

import org.parceler.Parcels;

import java.util.List;


public class HomeFragment extends CustomFragment {

    private MainActivity ma;
    private static HomeFragment instance;
    public static String TAG = "HOMEFRAGMENT";
    private List<Offer> offers;

    @Override
    protected boolean canGoBack() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_view, container, false);
        ma = (MainActivity) getActivity();
        RecyclerView mRecycleView = (RecyclerView) rootView.findViewById(R.id.home_recview);
        LinearLayoutManager llm = new LinearLayoutManager(ma);
        mRecycleView.setLayoutManager(llm);
        mRecycleView.addOnItemTouchListener(
                new RecyclerItemClickListener(ma, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(ma, OfferDetailActivity.class);
                        i.putExtra("Offer", Parcels.wrap(offers.get(position)));
                        startActivity(i);
                    }
                }));
        Profile profile = Parcels.unwrap(getArguments().getParcelable("profile"));
        if (profile != null) {
            offers = profile.getOffers();
            HomeRVAdapter mAdapter = new HomeRVAdapter(ma, offers);
            mRecycleView.setAdapter(mAdapter);
        } else {
            Log.i(TAG, "profile parcelable was null");
            Toast.makeText(ma, "Unable to load profile settings", Toast.LENGTH_SHORT).show();
        }
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