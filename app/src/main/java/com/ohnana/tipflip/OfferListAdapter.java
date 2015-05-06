package com.ohnana.tipflip;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Messikrkic on 28-04-2015.
 */
public class OfferListAdapter extends BaseAdapter {

    private List<Offer> offers;
    private Activity activity;
    private LayoutInflater inflater;

    public OfferListAdapter (Activity activity, List<Offer> offers) {
        this.activity = activity;
        this.offers = offers;
    }

    public void updateData(List<Offer> offers) {
        this.offers.clear();
        this.offers.addAll(offers);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return offers.size();
    }

    @Override
    public Object getItem(int position) {
        return offers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_offeritem, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.listoffer_cat);
        // getting category data for the row
        Offer o = offers.get(position);

        // Setting category
        title.setText(o.getCategory().getName());

        TextView discount = (TextView) convertView.findViewById(R.id.listoffer_disc);
        discount.setText(o.getDiscount());

        return convertView;

    }
}