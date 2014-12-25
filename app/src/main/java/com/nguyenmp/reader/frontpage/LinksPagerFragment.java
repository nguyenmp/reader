package com.nguyenmp.reader.frontpage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;

import com.nguyenmp.reader.ImmutableSubredditLinksPagerAdapter;
import com.nguyenmp.reader.R;
import com.nguyenmp.reader.adapters.LinksAdapter;
import com.nguyenmp.reader.two_pane.CollectionManager;
import com.nguyenmp.reader.two_pane.PagerFragment;
import com.nguyenmp.reddit.data.Link;

public class LinksPagerFragment extends PagerFragment<Link[]> implements LinksAdapter.Callback {

    public static LinksPagerFragment newInstance() {
        return new LinksPagerFragment();
    }

    @Override
    public void loadMore() {
        ((CollectionManager) getActivity()).loadMore();
    }

    @NonNull
    @Override
    public PagerAdapter getPagerAdapter(@NonNull Link[] collection) {
        return new ImmutableSubredditLinksPagerAdapter(collection, getFragmentManager(), this);
    }

    @Override
    public boolean isCollectionEmpty(@NonNull Link[] collection) {
        return collection.length == 0;
    }

    @Nullable
    @Override
    public String getEmptyText() {
        return getString(R.string.no_links_found);
    }
}
