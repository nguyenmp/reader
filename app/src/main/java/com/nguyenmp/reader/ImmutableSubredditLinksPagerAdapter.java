package com.nguyenmp.reader;

import android.support.v4.app.FixedFragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.nguyenmp.reader.adapters.LinksAdapter;
import com.nguyenmp.reddit.data.Link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImmutableSubredditLinksPagerAdapter extends FixedFragmentStatePagerAdapter {
    private Link[] listing;
    private final LinksAdapter.Callback callback;

    public ImmutableSubredditLinksPagerAdapter(Link[] listing, FragmentManager fm, LinksAdapter.Callback callback) {
        super(fm);
        this.listing = listing;
        this.callback = callback;
    }

    @Override
    public Fragment getItem(int position) {
        int lastItemIndex = listing.length - 1;
        if (position >= lastItemIndex - LinksAdapter.LOAD_MORE_THRESHOLD) callback.loadMore();
        Link link = listing[position];
        return CommentsFragment.newInstance(link);
    }

    @Override
    public int getCount() {
        return listing.length;
    }
}
