package com.ohnana.tipflip;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        mListView = (ListView) rootView.findViewById(R.id.listViewSubscribe);
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
                    progressView.setVisibility(View.GONE);
                    myAdapter.updateData(mAdapterItems);
                    getCategories();
                } else {
                    Toast.makeText(ma, "ADAPTERITEMS IS NULL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                showInternetAlert();
            }
        });
    }

    private void showInternetAlert() {
        new AlertDialog.Builder(ma)
                .setTitle("Internet error")
                .setMessage("No internet connection was found. Try again.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", null)
                .create().show();
        progressView.setVisibility(View.GONE);
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
                showInternetAlert();
            }
        });
    }

    private void addButton(final Category c) {
        final FloatingActionButton newButton = new FloatingActionButton(ma);
        String catTitle = c.getCategory();
        String output = catTitle.substring(0, 1).toUpperCase() + catTitle.substring(1);
        String base64Image = c.getImage();
        byte[] image = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        newButton.setImageBitmap(getCroppedBitmap(bitmap));
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

    private Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    private void loadButtons() {
        for (final Category cat : SubscribeFragment.this.categories) {
            addButton(cat);
        }
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
