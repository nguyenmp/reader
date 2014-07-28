package com.nguyenmp.reader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nguyenmp.reddit.data.Link;

import java.util.ArrayList;
import java.util.Arrays;

public class SubredditLinksAdapter extends BaseAdapter {
    private final ArrayList<Link> data = new ArrayList<Link>();
    private final Context context;

    public SubredditLinksAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Link getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Inflate a new view if we cannot recycle an old one
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Link link = getItem(position);

        TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        text1.setText(link.getData().getTitle());

        TextView text2 = (TextView) view.findViewById(android.R.id.text2);
        text2.setText(link.getData().getSubreddit());

        return view;
    }

    public void add(Link... newData) {
        data.addAll(Arrays.asList(newData));
        notifyDataSetChanged();
    }

    public void set(Link... newData) {
        data.clear();
        data.addAll(Arrays.asList(newData));
        notifyDataSetChanged();;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}