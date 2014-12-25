package com.nguyenmp.reader.two_pane;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nguyenmp.reader.R;
import com.nguyenmp.reader.util.ListFragmentSwipeRefreshLayout;

public abstract class LoadableListFragment<CollectionType>
        extends ListFragment<CollectionType>
        implements SwipeRefreshLayout.OnRefreshListener {

    private ListFragmentSwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View list_content = super.onCreateView(inflater, container, savedInstanceState);
        Context context = inflater.getContext();
        ListView list_view = (ListView) list_content.findViewById(R.id.list_view);

        // Now create a SwipeRefreshLayout to wrap the fragment's content view
        refreshLayout = new ListFragmentSwipeRefreshLayout(context, list_view);

        // Add the list fragment's content view to the
        // SwipeRefreshLayout, making sure that it fills
        // the SwipeRefreshLayout
        refreshLayout.addView(list_content,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Make sure that the SwipeRefreshLayout will fill the fragment
        refreshLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        // Sets color scheme resources
        refreshLayout.setColorSchemeResources(R.color.reddit_orange, R.color.ui_text, R.color.orangered, R.color.neutral);

        // Now return the SwipeRefreshLayout as this fragment's content view
        return refreshLayout;
    }

    @Override
    public void setLoading(boolean isLoading) {
        refreshLayout.setRefreshing(true);
    }

    /**
     * Set the {@link android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener} to listen for
     * initiated refreshes.
     *
     * @see android.support.v4.widget.SwipeRefreshLayout#setOnRefreshListener(android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener)
     */
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        refreshLayout.setOnRefreshListener(listener);
    }

    public void loadMore() {
        ((CollectionManager) getActivity()).loadMore();
    }

    @Override
    public void onRefresh() {
        ((CollectionManager) getActivity()).refresh();
    }

    @NonNull
    @Override
    public ListAdapter wrapListAdapter(ListAdapter originalListAdapter) {
        return new LoadMoreAdapter(originalListAdapter, this);
    }

    private static class LoadMoreAdapter extends BaseAdapter {
        private static final int THRESHOLD = 5;

        private final ListAdapter delegate;
        private final LoadableListFragment fragment;

        private LoadMoreAdapter(ListAdapter delegate, LoadableListFragment fragment) {
            this.delegate = delegate;
            this.fragment = fragment;
        }

        @Override
        public int getCount() {
            return delegate.getCount();
        }

        @Override
        public Object getItem(int position) {
            return delegate.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return delegate.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if ((getCount() - position) <= THRESHOLD) fragment.loadMore();

            return delegate.getView(position, convertView, parent);
        }
    }
}
