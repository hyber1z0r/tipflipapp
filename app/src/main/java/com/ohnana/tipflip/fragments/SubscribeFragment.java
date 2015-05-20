package com.ohnana.tipflip.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import com.ohnana.tipflip.adapters.CategoryListAdapter;
import com.ohnana.tipflip.MainActivity;
import com.ohnana.tipflip.listeners.ProfileSaveHandler;
import com.ohnana.tipflip.model.Profile;
import com.ohnana.tipflip.R;
import com.ohnana.tipflip.model.Category;

import org.parceler.Parcels;

import java.util.List;

public class SubscribeFragment extends CustomFragment {

    private MainActivity ma;
    private static SubscribeFragment instance;
    public static String TAG = "SUBSCRIBEFRAGMENT";
    private Profile profile;
    private List<Category> categories; // holds the categories in the button menu
    private FloatingActionsMenu mFloatingsMenu;
    private CategoryListAdapter myAdapter;
    private ProfileSaveHandler mCallback; // this is not even used, but it still works. why???
    // can i remove it? that would be nice.

    public SubscribeFragment() {
    }

    public static SubscribeFragment getInstance() {
        if (instance == null) {
            instance = new SubscribeFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subscribe_view, container, false);
        ma = (MainActivity) getActivity();
        init(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ProfileSaveHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ProfileSaveHandler");
        }
    }

    private void init(View rootView) {
        Profile p = Parcels.unwrap(getArguments().getParcelable("profile"));
        List<Category> cats = Parcels.unwrap(getArguments().getParcelable("categories"));
        if (p == null || cats == null) {
            Toast.makeText(ma, "There was an error loading profile info.", Toast.LENGTH_SHORT).show();
        } else {
            this.profile = p;
            this.categories = cats;
            List<Category> mAdapterItems = this.profile.getCategories();
            final ListView mListView = (ListView) rootView.findViewById(R.id.listViewSubscribe);
            myAdapter = new CategoryListAdapter(ma, mAdapterItems);
            mListView.setAdapter(myAdapter);

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
                            myAdapter.remove(i);
                            ma.hideSaveMenuItem(false);
                            // should i set the profile.categories ?? we'll see
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
                    }
                }
            });

            mFloatingsMenu = (FloatingActionsMenu) rootView.findViewById(R.id.multiple_actions);

            // Only add those that the users doesn't subscribe to, to the button menu.
            for (Category cat : mAdapterItems) {
                for (int i = 0; i < this.categories.size(); i++) {
                    if (cat.getName().equals(this.categories.get(i).getName())) {
                        this.categories.remove(i);
                    }
                }
            }
            loadButtons();
        }
    }

    private void addButton(final Category c) {
        final FloatingActionButton newButton = new FloatingActionButton(ma);
        String catTitle = c.getName();
        String output = catTitle.substring(0, 1).toUpperCase() + catTitle.substring(1);
        String base64Image = c.getImage();
        byte[] image = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        newButton.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 112, 122, false));
        newButton.setTitle(output);
        newButton.setColorNormalResId(R.color.white);
        newButton.setColorPressedResId(R.color.white_pressed);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAdapter.add(c);
                mFloatingsMenu.collapse();
                mFloatingsMenu.removeButton(newButton);
                ma.hideSaveMenuItem(false);
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
        for (Category cat : this.categories) {
            addButton(cat);
        }
    }

    @Override
    protected boolean canGoBack() {
        return true;
    }
}
