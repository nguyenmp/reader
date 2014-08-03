package com.nguyenmp.reader;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.nguyenmp.reader.adapters.SubredditLinksAdapter;
import com.nguyenmp.reader.util.SwipeRefreshListFragment;
import com.nguyenmp.reddit.data.Link;
import com.nguyenmp.reddit.data.SubredditLinkListing;

public class SubredditLinkFragment extends SwipeRefreshListFragment
        implements Refreshable,
        LoaderManager.LoaderCallbacks<SubredditLinkListing>,
        SwipeRefreshLayout.OnRefreshListener,
        SubredditLinksAdapter.Callback {

    public static final String ARGUMENT_LINK_ID = "argument_link";

    private static final int LOADER_ID = 0;

    public static SubredditLinkFragment newInstance(String link_id) {
        SubredditLinkFragment fragment = new SubredditLinkFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_LINK_ID, link_id);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static SubredditLinkFragment newInstance(Link link) {
        return newInstance(link.getData().getId());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        getListView().setDividerHeight(0);
        getListView().setBackgroundColor(getResources().getColor(R.color.cards_background));
        getSwipeRefreshLayout().setBackgroundColor(getResources().getColor(R.color.cards_background));
        setEmptyText(getString(R.string.empty_subreddit));
        setListAdapter(new SubredditLinksAdapter(getActivity(), this));
        setListShown(false);
        setRefreshing(true);
        if (savedInstanceState == null) refresh();
        else {
//            Bundle args = new Bundle();
//            args.putString(ARGUMENT_SUBREDDIT, mSubreddit);
//            getLoaderManager().initLoader(LOADER_ID, args, this);
        }
        setOnRefreshListener(this);
    }

    @Override
    public SubredditLinksAdapter getListAdapter() {
        return (SubredditLinksAdapter) super.getListAdapter();
    }

    @Override
    public void refresh() {
        setRefreshing(true);
        setListShown(false);
        Bundle args = new Bundle();

        getLoaderManager().restartLoader(LOADER_ID, args, this);
    }

    @Override
    public Loader<SubredditLinkListing> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<SubredditLinkListing> loader, SubredditLinkListing data) {
        if (data != null) getListAdapter().set(data.getData().getChildren());
        setListShown(true);
        setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<SubredditLinkListing> loader) {
        getListAdapter().clear();
        setRefreshing(true);
    }

    @Override
    public void loadMore() {
        getLoaderManager().getLoader(LOADER_ID).onContentChanged();
        setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        refresh();
    }
}
