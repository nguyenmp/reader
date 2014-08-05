package com.nguyenmp.reader;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.nguyenmp.reader.adapters.CommentsAdapter;
import com.nguyenmp.reader.loaders.CommentsLoader;
import com.nguyenmp.reader.util.SwipeRefreshListFragment;
import com.nguyenmp.reddit.data.Comments;
import com.nguyenmp.reddit.data.Link;

public class CommentsFragment extends SwipeRefreshListFragment
        implements Refreshable,
        LoaderManager.LoaderCallbacks<Comments>,
        SwipeRefreshLayout.OnRefreshListener,
        CommentsAdapter.Callback {

    public static final String ARGUMENT_LINK_ID = "argument_link";

    private static final int LOADER_ID = 0;

    public static CommentsFragment newInstance(String link_id) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_LINK_ID, link_id);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static CommentsFragment newInstance(Link link) {
        return newInstance(link.getData().getId());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        getListView().setDividerHeight(0);
        getListView().setBackgroundColor(getResources().getColor(R.color.cards_background));
        getSwipeRefreshLayout().setBackgroundColor(getResources().getColor(R.color.cards_background));
        setEmptyText("No Content Found");

        if (savedInstanceState == null) refresh();
        else {
            Bundle args = new Bundle();
            args.putString(ARGUMENT_LINK_ID, getArguments().getString(ARGUMENT_LINK_ID));
            getLoaderManager().initLoader(LOADER_ID, args, this);
        }
        setOnRefreshListener(this);
    }

    @Override
    public CommentsAdapter getListAdapter() {
        return (CommentsAdapter) super.getListAdapter();
    }

    @Override
    public void refresh() {
        setRefreshing(true);
        setListShown(false);
        getLoaderManager().restartLoader(LOADER_ID, getArguments(), this);
    }

    @Override
    public Loader<Comments> onCreateLoader(int id, Bundle args) {
        return new CommentsLoader(getActivity(), args.getString(ARGUMENT_LINK_ID));
    }

    @Override
    public void onLoadFinished(Loader<Comments> loader, Comments data) {
        if (data != null) setListAdapter(new CommentsAdapter(getActivity(), data));
        setListShown(true);
        setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Comments> loader) {
        setListAdapter(null);
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
