package com.ohnana.tipflip;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.ohnana.tipflip.interfaces.TipFlipService;

import org.json.JSONObject;

import java.net.UnknownHostException;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


public class SplashScreen extends ActionBarActivity {
    private TipFlipService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://tipflip.herokuapp.com")
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        api = restAdapter.create(TipFlipService.class);
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    api.pingServer();
                } catch (Exception ex) {
                    return -1;
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer resultCode) {
                super.onPostExecute(resultCode);
                if(resultCode != -1) {
                    Intent i = new Intent("com.ohnana.tipflip.MAINACTIVITY");
                    startActivity(i);
                } else {
                    new AlertDialog.Builder(SplashScreen.this)
                            .setTitle("Error")
                            .setMessage("Could not connect to the internet")
                            .setNeutralButton("OK", null)
                            .create()
                            .show();
                }

            }
        }.execute(null, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
