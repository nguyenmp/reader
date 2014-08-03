package com.nguyenmp.reader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nguyenmp.reader.adapters.SubredditLinksAdapter;
import com.nguyenmp.reddit.data.Link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubredditLinksPagerAdapter extends FragmentStatePagerAdapter {
    private List<Link> listing = new ArrayList<Link>();
    private final SubredditLinksAdapter.Callback callback;

    public SubredditLinksPagerAdapter(FragmentManager fm, SubredditLinksAdapter.Callback callback) {
        super(fm);
        this.callback = callback;
    }

    @Override
    public Fragment getItem(int position) {
        int lastItemIndex = listing.size() - 1;
        if (position >= lastItemIndex - SubredditLinksAdapter.LOAD_MORE_THRESHOLD) callback.loadMore();
        Link link = listing.get(position);
        return SubredditLinkFragment.newInstance(link);
    }

    @Override
    public int getCount() {
        return listing.size();
    }

    public void set(Link... links) {
        listing.clear();
        listing.addAll(Arrays.asList(links));
        notifyDataSetChanged();
    }

    public void clear() {
        listing.clear();
        notifyDataSetChanged();
    }
}
