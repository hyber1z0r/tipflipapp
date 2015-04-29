package com.ohnana.tipflip;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.IconTextView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jakobgaardandersen on 29/04/15.
 */
public class DrawerListItemAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DrawerListItem> items;

    public DrawerListItemAdapter(Activity activity, List<DrawerListItem> items) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
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
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
        }

        // the item
        DrawerListItem item = items.get(position);

        // icon
        IconTextView icon = (IconTextView) convertView.findViewById(R.id.drawerlistitem_icon);
        icon.setText(item.getIcon());

        // title
        TextView title = (TextView) convertView.findViewById(R.id.drawerlistitem_text);
        title.setText(item.getTitle());

        return convertView;
    }
}
