package com.ohnana.tipflip.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.ohnana.tipflip.R;
import com.ohnana.tipflip.model.Offer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jakobgaardandersen on 29/04/15.
 */
public class HomeRVAdapter extends RecyclerView.Adapter<HomeRVAdapter.OfferViewHolder> {
    private List<Offer> offers;
    private Context activity;

    public HomeRVAdapter(Activity activity, List<Offer> offers) {

        this.activity = activity;
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
        holder.rabat.setText(o.getDiscount());
        holder.desc.setText("Der er " + o.getDiscount() + " på " + o.getOffer());

        Date exp = o.getExpiration();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'kl.' HH:mm", Locale.ENGLISH);
        holder.expDate.setText(formatter.format(exp));

        if (exp.before(new Date())) {
            holder.status.setTextColor(activity.getResources().getColor(R.color.notactive_offer));
            holder.status.setText("expired");
        } else {
            holder.status.setTextColor(activity.getResources().getColor(R.color.active_offer));
            holder.status.setText("active");
        }

        String base64Image = o.getStore().getImage();
        byte[] image = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.butikPhoto.setImageBitmap(bitmap);

        base64Image = o.getImage();
        if (base64Image != null) {
            image = Base64.decode(base64Image, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.offerPhoto.setImageBitmap(bitmap);
        } else {
            // for now, set the app icon if no offer photo is available
            holder.offerPhoto.setImageResource(R.mipmap.ic_launcher_tf);
        }

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
        return offers == null ? -1 : offers.size();
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView rabat;
        TextView center;
        TextView butik;
        TextView desc;
        ImageView butikPhoto;
        ImageView offerPhoto;
        TextView expDate;
        TextView status;

        OfferViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.home_card_view);
            center = (TextView) itemView.findViewById(R.id.cardview_center);
            butik = (TextView) itemView.findViewById(R.id.cardview_butik);
            rabat = (TextView) itemView.findViewById(R.id.cardview_rabat);
            butikPhoto = (ImageView) itemView.findViewById(R.id.cardview_imageStore);
            offerPhoto = (ImageView) itemView.findViewById(R.id.cardview_imageOffer);
            desc = (TextView) itemView.findViewById(R.id.cardview_desc);
            expDate = (TextView) itemView.findViewById(R.id.cardview_exp);
            status = (TextView) itemView.findViewById(R.id.cardview_status);
        }
    }
}
