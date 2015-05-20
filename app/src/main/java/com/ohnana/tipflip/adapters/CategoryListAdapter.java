package com.ohnana.tipflip.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ohnana.tipflip.R;
import com.ohnana.tipflip.model.Category;

import java.util.List;

/**
 * Created by jakobgaardandersen on 25/04/15.
 */
public class CategoryListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Category> categories;

    public CategoryListAdapter(Activity activity, List<Category> categories) {
        this.activity = activity;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories == null ? -1 : categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public synchronized void remove(int position) {
        this.categories.remove(position);
        this.notifyDataSetChanged();
    }

    public synchronized void add(Category c) {
        this.categories.add(c);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_catitem, null);
        }
        // getting category data for the row
        Category c = categories.get(position);

        // title
        String catTitle = c.getName();
        String output = catTitle.substring(0, 1).toUpperCase() + catTitle.substring(1);
        TextView title = (TextView) convertView.findViewById(R.id.list_item_text);
        title.setText(output);

        // image
        String base64Image = c.getImage();
        byte[] image = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_image);
        imageView.setImageBitmap(bitmap);

        return convertView;
    }
}
