package com.nguyenmp.reader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.nguyenmp.reader.adapters.SubredditLinksAdapter;
import com.nguyenmp.reader.loaders.SubredditLinksLoader;
import com.nguyenmp.reader.util.SwipeRefreshListFragment;
import com.nguyenmp.reddit.data.Link;
import com.nguyenmp.reddit.data.SubredditLinkListing;

public class SubredditLinkListingFragment extends SwipeRefreshListFragment
        implements Refreshable,
        LoaderManager.LoaderCallbacks<SubredditLinkListing>,
        SwipeRefreshLayout.OnRefreshListener,
        SubredditLinksAdapter.Callback {

    /** Specifies the subreddit for this fragment to display the listing of.
     * If not specified, this Fragment will simply show the frontpage. */
    public static final String ARGUMENT_SUBREDDIT = "com.nguyenmp.reader.SubredditLinkListing.ARGUMENT_SUBREDDIT";
    public static final String STATE_SUBREDDIT = "state_subreddit";

    private static final int LOADER_ID = 0;

    private String mSubreddit;

    public static SubredditLinkListingFragment newInstance() {
        return newInstance(null);
    }

    public static SubredditLinkListingFragment newInstance(String subreddit) {
        SubredditLinkListingFragment fragment = new SubredditLinkListingFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_SUBREDDIT, subreddit);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle inState) {
        super.onCreate(inState);
        mSubreddit = getArguments().getString(ARGUMENT_SUBREDDIT);
        if (inState != null) mSubreddit = inState.getString(STATE_SUBREDDIT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STATE_SUBREDDIT, mSubreddit);
    }

    public void setSubreddit(String subreddit) {
        this.mSubreddit = subreddit;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Link link = getListAdapter().getItem(position);

        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setData(Uri.parse(link.getData().getUrl()));
        startActivity(viewIntent);
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
            Bundle args = new Bundle();
            args.putString(ARGUMENT_SUBREDDIT, mSubreddit);
            getLoaderManager().initLoader(LOADER_ID, args, this);
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
        args.putString(ARGUMENT_SUBREDDIT, mSubreddit);
        getLoaderManager().restartLoader(LOADER_ID, args, this);
    }

    @Override
    public Loader<SubredditLinkListing> onCreateLoader(int id, Bundle args) {
        return new SubredditLinksLoader(getActivity(), args.getString(ARGUMENT_SUBREDDIT));
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
