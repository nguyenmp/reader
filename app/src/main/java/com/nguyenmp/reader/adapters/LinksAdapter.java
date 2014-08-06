package com.nguyenmp.reader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nguyenmp.reader.R;
import com.nguyenmp.reddit.data.Link;

import java.util.ArrayList;
import java.util.Arrays;

public class LinksAdapter extends BaseAdapter {
    public static final int LOAD_MORE_THRESHOLD = 5;  // Load more when we are 5 items from the bottom

    private final ArrayList<Link> data = new ArrayList<Link>();
    private final Context context;
    private final Callback callback;

    public static interface Callback {
        public void loadMore();
    }

    public LinksAdapter(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
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
        // Load more if we're showing the last view
        int lastItemIndex = data.size() - 1;
        if (position >= (lastItemIndex) - LOAD_MORE_THRESHOLD && callback != null) callback.loadMore();

        // Inflate a new view if we cannot recycle an old one
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.list_item_link, parent, false);
        }

        Link link = getItem(position);

        TextView text1 = (TextView) view.findViewById(R.id.list_item_link_title);
        text1.setText(link.getData().getTitle());

        TextView text2 = (TextView) view.findViewById(R.id.list_item_link_subtitle);
        text2.setText(link.getData().getSubreddit());

        return view;
    }

    public Link[] getData() {
        return data.toArray(new Link[data.size()]);
    }

    public void add(Link... newData) {
        data.addAll(Arrays.asList(newData));
        notifyDataSetChanged();
    }

    public void set(Link... newData) {
        data.clear();
        data.addAll(Arrays.asList(newData));
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}