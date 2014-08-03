package com.nguyenmp.reader;

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

public class SubredditLinkFragment extends SwipeRefreshListFragment
        implements Refreshable,
        LoaderManager.LoaderCallbacks<SubredditLinkListing>,
        SwipeRefreshLayout.OnRefreshListener,
        SubredditLinksAdapter.Callback {

    public static final String ARGUMENT_LINKS = "argument_links";
    public static final String ARGUMENT_POSITION = "argument_position";
    public static final String STATE_POSITION = "state_position";

    private static final int LOADER_ID = 0;

    private Link[] mLinks;
    private int mPosition;

    public static SubredditLinkFragment newInstance(Link link) {
        // It's like showing only one link (instead of multiple links)
        return newInstance(new Link[] {link}, 0);
    }

    public static SubredditLinkFragment newInstance(Link[] links, int position) {
        SubredditLinkFragment fragment = new SubredditLinkFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARGUMENT_LINKS, links);
        arguments.putInt(ARGUMENT_POSITION, position);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle inState) {
        super.onCreate(inState);
        Bundle arguments = getArguments();
        mLinks = (Link[]) arguments.getSerializable(ARGUMENT_LINKS);
        mPosition = arguments.getInt(ARGUMENT_POSITION);
        if (inState != null) mPosition = inState.getInt(STATE_POSITION);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STATE_POSITION, mPosition);
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
//        return new SubredditLinksLoader(getActivity(), args.getString(ARGUMENT_SUBREDDIT));
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
