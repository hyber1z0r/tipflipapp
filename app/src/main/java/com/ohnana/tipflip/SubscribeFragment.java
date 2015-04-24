package com.ohnana.tipflip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import com.hudomju.swipe.adapter.ViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SubscribeFragment extends CustomFragment implements View.OnClickListener {

    private MainActivity ma;
    private static SubscribeFragment instance;
    public static String TAG = "SUBSCRIBEFRAGMENT";
    private TipFlipService service;
    private User user;
    private List<String> categories;
    private ListView mListView;
    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscribe_view, container, false);
        int[] clickButtons = new int[]{ // buttons
        };
        for (int i : clickButtons) {
            rootView.findViewById(i).setOnClickListener(this);
        }
        ma = (MainActivity) getActivity();
        init(rootView);
        loadProfile();
        getCategories();
        return rootView;
    }

    private void init(View rootView) {
        final List<String> items = new ArrayList<>();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
        items.add("Item 4");
        mListView = (ListView) rootView.findViewById(R.id.listView);
        final ArrayAdapter<String> myAdapter = new ArrayAdapter<>(ma, R.layout.listview_item, R.id.txt_data, items);
        mListView.setAdapter(myAdapter);

        final SwipeToDismissTouchListener touchListener = new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(mListView),
                        new SwipeToDismissTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ViewAdapter viewAdapter, int i) {
                                items.remove(i);
                                myAdapter.notifyDataSetChanged();
                            }
                        });
        mListView.setOnTouchListener(touchListener);
        mListView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    //Toast.makeText(ListViewActivity.this, "Position " + position, LENGTH_SHORT).show();
                }
            }
        });
        // at bottom
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://tipflip.herokuapp.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        service = restAdapter.create(TipFlipService.class);

    }

    private void loadProfile() {
        service.getProfile("Jakob", new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                SubscribeFragment.this.user = user;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void getCategories() {
        service.getCategories(new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, Response response) {
                SubscribeFragment.this.categories = strings;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    public SubscribeFragment() {
    }

    public static SubscribeFragment getInstance() {
        if (instance == null) {
            instance = new SubscribeFragment();
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
