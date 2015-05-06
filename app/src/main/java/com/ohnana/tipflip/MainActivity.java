package com.ohnana.tipflip;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, DownloadObserver {

    /**
     * Google Cloud Messaging attributes
     */
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCM";
    private String SENDER_ID = "1070134966617"; // PROJECT NUMBER
    private GoogleCloudMessaging gcm;
    private Context context;
    private String regid;
    private TipFlipService service;

    /**
     * Location attributes
     */
    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * Navigation drawer attributes
     */
    private List<DrawerListItem> mDrawerTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Our attributes
     */
    private Profile profile;
    private List<Offer> offers;
    private List<Category> categories;
    private DataDownloadedListener downloadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        init();
        loadProfile();
        loadOffers();
        loadCategories();
    }

    private void init() {
        /**
         * The navigation drawer setup
         * */
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        getSupportActionBar().setIcon(R.drawable.ic_drawer);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerTitles = new ArrayList<>();
        mDrawerTitles.add(new DrawerListItem("{fa-home}", "Home"));
        mDrawerTitles.add(new DrawerListItem("{fa-user}", "Profile"));
        mDrawerTitles.add(new DrawerListItem("{fa-plus-circle}", "Subscribe"));
        mDrawerTitles.add(new DrawerListItem("{fa-newspaper-o}", "Offers"));
        mDrawerTitles.add(new DrawerListItem("{fa-location-arrow}", "Location"));
        mDrawerTitles.add(new DrawerListItem("{fa-sign-out}", "Sign out"));
        mDrawerTitles.add(new DrawerListItem("", "Send postrequest"));

        DrawerListItemAdapter drawerAdapter = new DrawerListItemAdapter(this, mDrawerTitles);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mTitle);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /**
         * The Google Play Services and location setup
         * */
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            Log.i(TAG, regid);
            if (regid.isEmpty()) {
                registerInBackground();
            }
            buildGoogleApiClient();
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        /**
         * The REST Adapter setup, for making http calls to our Node.js server
         * */
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://tipflip.herokuapp.com")
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setConverter(new GsonConverter(gson))
                .build();
        service = restAdapter.create(TipFlipService.class);
        getLocation();

        downloadListener = new DataDownloadedListener();
        downloadListener.registerObserver(this);
    }

    private void loadProfile() {
        service.getProfile(getRegistrationId(this), new Callback<Profile>() {
            @Override
            public void success(Profile profile, Response response) {
                MainActivity.this.profile = profile;
                downloadListener.done();
            }

            @Override
            public void failure(RetrofitError error) {
                showError("profile", error);
            }
        });
    }

    private void loadOffers() {
        service.getAllOffers(new Callback<List<Offer>>() {
            @Override
            public void success(List<Offer> offers, Response response) {
                MainActivity.this.offers = offers;
                downloadListener.done();
            }

            @Override
            public void failure(RetrofitError error) {
                showError("offers", error);
            }
        });
    }

    private void loadCategories() {
        service.getCategories(new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                MainActivity.this.categories = categories;
                downloadListener.done();
            }

            @Override
            public void failure(RetrofitError error) {
                showError("categories", error);
            }
        });
    }


    private void showError(String type, RetrofitError error) {
        Toast.makeText(this, "Error in getting " + type + ": " + error.getMessage(), Toast.LENGTH_LONG).show();
        Log.i(TAG, error.getMessage());
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    private Location getLocation() {
        connectToGooglePlay();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            // Toast.makeText(context, "Your location: " + latitude + " ," + longitude, Toast.LENGTH_LONG).show();
            return mLastLocation;
        } else {
            // no location available -> check phone settings
            return null;
        }
    }

    private void connectToGooglePlay() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    /**
     * These 2 assures that the ic_drawer icon is drawn somehow
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void update() {
        selectItem(0); // the first: Home fragment
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment
        Fragment fragment;
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                fragment = HomeFragment.getInstance();
                bundle.putParcelable("profile", Parcels.wrap(profile));
                break;
            case 1:
                fragment = ProfileFragment.getInstance();
                break;
            case 2:
                fragment = SubscribeFragment.getInstance();
                bundle.putParcelable("profile", Parcels.wrap(profile));
                bundle.putParcelable("categories", Parcels.wrap(categories));
                break;
            case 3:
                fragment = OffersFragment.getInstance();
                bundle.putParcelable("offers", Parcels.wrap(offers));
                break;
            case 4:
                fragment = LocationFragment.getInstance();
                break;
            case 5:
                // log out!
                // homefragment because of failure -> should be login fragment in future
                fragment = HomeFragment.getInstance();
                break;
            case 6:
                // fall through to default!
                sendRegistrationIdToBackend();
            default:
                fragment = HomeFragment.getInstance();
        }
        fragment.setArguments(bundle);
        // Insert the fragment by replacing any existing fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles.get(position).getTitle());
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    // Send to webserver
                    sendRegistrationIdToBackend();
                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg);
            }
        }.execute(null, null, null);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private void sendRegistrationIdToBackend() {
        HashMap<String, String> map = new HashMap<>();
        map.put("regid", regid);
        service.saveRegId(map, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response) {
                Log.i(TAG, "Successfully registered regid to node.js server");
                Toast.makeText(MainActivity.this, "Success in registereing id", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error in contacting node.js server");
                new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Internet error")
                        .setMessage("Was unable to register device. Contact app admin")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", null)
                        .create().show();
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkPlayServices();
        getLocation();
    }
}
