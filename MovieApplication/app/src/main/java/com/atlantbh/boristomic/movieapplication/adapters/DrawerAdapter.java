package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.models.DrawerItem;

import java.util.List;

/**
 * Created by boristomic on 30/01/16.
 */
public class DrawerAdapter extends BaseAdapter {

    private List<DrawerItem> drawerItems;

    public DrawerAdapter(List<DrawerItem> drawerItems) {
        this.drawerItems = drawerItems;
    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View initConverView(final Context context, final View convertView, final ViewGroup viewGroup) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            final View view = layoutInflater.inflate(R.layout.drawer_list_item, viewGroup, false);
            return view;
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final View view = initConverView(context, convertView, parent);
        final ImageView drawerIcon = (ImageView) view.findViewById(R.id.drawer_item_icon);
        final TextView drawerTitle = (TextView) view.findViewById(R.id.drawer_item_title);
        final DrawerItem drawerItem = drawerItems.get(position);
        drawerIcon.setImageResource(drawerItem.getIcon());
        drawerTitle.setText(drawerItem.getTitle());

        return view;
    }
}
