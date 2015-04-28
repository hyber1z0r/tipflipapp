package com.ohnana.tipflip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import com.hudomju.swipe.adapter.ViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * TODO: Fix when swiping item, to add back to buttongroup.
 */

public class SubscribeFragment extends CustomFragment {

    private MainActivity ma;
    private static SubscribeFragment instance;
    public static String TAG = "SUBSCRIBEFRAGMENT";
    private TipFlipService service;
    private User user;
    private List<Category> categories;
    private ListView mListView;
    private FloatingActionsMenu mFloatingsMenu;
    private List<Category> mAdapterItems = new ArrayList<>();
    private CategoryListAdapter myAdapter;
    private CircularProgressView progressView;

    @Override
    protected boolean canGoBack() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscribe_view, container, false);
        ma = (MainActivity) getActivity();
        init(rootView);
        loadProfile();
        return rootView;
    }

    private void init(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.listView);
        myAdapter = new CategoryListAdapter(ma, mAdapterItems);
        mListView.setAdapter(myAdapter);
        progressView = (CircularProgressView) rootView.findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener = new SwipeToDismissTouchListener<>(
                new ListViewAdapter(mListView),
                new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListViewAdapter listViewAdapter, int i) {
                        Category temp = (Category) myAdapter.getItem(i);
                        categories.add(temp);
                        addButton(temp);
                        mAdapterItems.remove(i);
                        myAdapter.updateData(mAdapterItems);
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

        mFloatingsMenu = (FloatingActionsMenu) rootView.findViewById(R.id.multiple_actions);

        // at bottom
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://tipflip.herokuapp.com")
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        service = restAdapter.create(TipFlipService.class);
    }

    private void loadProfile() {
        progressView.startAnimation();
        service.getProfile("Jakob", new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                SubscribeFragment.this.user = user;
                if (mAdapterItems != null) {
                    mAdapterItems = SubscribeFragment.this.user.getCategories();
                    myAdapter.updateData(mAdapterItems);
                    getCategories();
                } else {
                    Toast.makeText(ma, "ADAPTERITEMS IS NULL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void getCategories() {
        service.getCategories(new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                SubscribeFragment.this.categories = categories;

                for (Category cat : mAdapterItems) {
                    for (int i = 0; i < SubscribeFragment.this.categories.size(); i++) {
                        if (cat.getCategory().equals(SubscribeFragment.this.categories.get(i).getCategory())) {
                            SubscribeFragment.this.categories.remove(i);
                        }
                    }
                }
                loadButtons();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void addButton(final Category c) {
        final FloatingActionButton newButton = new FloatingActionButton(ma);
        String catTitle = c.getCategory();
        String output = catTitle.substring(0, 1).toUpperCase() + catTitle.substring(1);
        newButton.setTitle(output);
        newButton.setColorNormalResId(R.color.white);
        newButton.setColorPressedResId(R.color.white_pressed);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterItems.add(c);
                mFloatingsMenu.collapse();
                mFloatingsMenu.removeButton(newButton);
                myAdapter.updateData(mAdapterItems);
            }
        });

        mFloatingsMenu.addButton(newButton);
    }

    private void loadButtons() {
        for (final Category cat : SubscribeFragment.this.categories) {
            addButton(cat);
        }
        progressView.setVisibility(View.GONE);
    }


    public SubscribeFragment() {
    }

    public static SubscribeFragment getInstance() {
        if (instance == null) {
            instance = new SubscribeFragment();
        }
        return instance;
    }
}
