package com.ohnana.tipflip;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jakobgaardandersen on 29/04/15.
 */
public class HomeRVAdapter extends RecyclerView.Adapter<HomeRVAdapter.OfferViewHolder> {
    private List<Offer> offers;

    public HomeRVAdapter(List<Offer> offers) {
        this.offers = offers;
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cardview, parent, false);
        OfferViewHolder pvh = new OfferViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        Offer o = offers.get(position);
        holder.center.setText("Lyngby Storcenter");
        holder.butik.setText("Matas");
        holder.rabat.setText("Rabat " + o.getDiscount());
        holder.desc.setText("Der er " + o.getDiscount() + " på " + o.getCategory().getCategory() + " mellem kl. 12 og 14");
        holder.butikPhoto.setImageResource(R.drawable.matas_logo);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView rabat;
        TextView center;
        TextView butik;
        TextView desc;
        ImageView butikPhoto;

        OfferViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.home_card_view);
            center = (TextView) itemView.findViewById(R.id.cardview_center);
            butik = (TextView) itemView.findViewById(R.id.cardview_butik);
            rabat = (TextView) itemView.findViewById(R.id.cardview_rabat);
            butikPhoto = (ImageView) itemView.findViewById(R.id.cardview_image);
            desc = (TextView) itemView.findViewById(R.id.cardview_desc);
        }
    }
}
