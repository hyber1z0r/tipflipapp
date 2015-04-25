package com.ohnana.tipflip;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.txt_data);
        // getting category data for the row
        Category c = categories.get(position);
        String catTitle = c.getCategory();
        String output = catTitle.substring(0, 1).toUpperCase() + catTitle.substring(1);
        // title
        title.setText(output);

        return convertView;
    }
}
