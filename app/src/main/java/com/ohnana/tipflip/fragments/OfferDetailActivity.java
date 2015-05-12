package com.ohnana.tipflip.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ohnana.tipflip.R;
import com.ohnana.tipflip.model.Offer;
import com.ohnana.tipflip.model.Store;

import org.parceler.Parcels;
import org.parceler.transfuse.annotations.OnBackPressed;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class OfferDetailActivity extends ActionBarActivity {

    private TextView expview;
    private TextView startview;
    private TextView description;
    private TextView storename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM | android.support.v7.app.ActionBar.DISPLAY_SHOW_HOME | android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Offer value = Parcels.unwrap(extras.getParcelable("Offer"));
            String title = value.getOffer();
            Date start = value.getCreated();
            Date exp = value.getExpiration();
            String discount = value.getDiscount();
            Store store = value.getStore();



            SimpleDateFormat sdfDenmark = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            sdfDenmark.setTimeZone(TimeZone.getDefault());
            String startfor = sdfDenmark.format(start);
            String startexp = sdfDenmark.format(exp);

            SpannableString ss =  new SpannableString("Der er tilbud på " + title + " Du kan sparer helt op til " + discount + "!");
            ss.setSpan(new ForegroundColorSpan(Color.BLUE), (ss.length()-5), (ss.length()-1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);




            getSupportActionBar().setTitle(title);
            expview = (TextView) findViewById(R.id.textViewExp);
            startview = (TextView) findViewById(R.id.textViewstart);
            description = (TextView) findViewById(R.id.textViewDescription);
            storename = (TextView) findViewById(R.id.textViewStore);

            startview.setText("Oprettet: " + startfor);
            expview.setText("Udløber: " + startexp);
            storename.setText(store.getName());
            description.setText(ss);



        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offer_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
