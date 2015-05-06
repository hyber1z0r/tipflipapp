package com.ohnana.tipflip;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        return new OfferViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        Offer o = offers.get(position);

        holder.center.setText("Lyngby Storcenter");
        holder.butik.setText(o.getStore().getName());
        holder.rabat.setText("Rabat " + o.getDiscount());
        holder.desc.setText("Der er " + o.getDiscount() + " på " + o.getOffer());
        Date exp = o.getExpiration();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        holder.expDate.setText(formatter.format(exp));
        String base64Image = o.getStore().getImage();
        byte[] image = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.butikPhoto.setImageBitmap(bitmap);
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(holder.cv);
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
        TextView expDate;

        OfferViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.home_card_view);
            center = (TextView) itemView.findViewById(R.id.cardview_center);
            butik = (TextView) itemView.findViewById(R.id.cardview_butik);
            rabat = (TextView) itemView.findViewById(R.id.cardview_rabat);
            butikPhoto = (ImageView) itemView.findViewById(R.id.cardview_image);
            desc = (TextView) itemView.findViewById(R.id.cardview_desc);
            expDate = (TextView) itemView.findViewById(R.id.cardview_exp);
        }
    }
}
