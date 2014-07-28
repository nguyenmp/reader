package com.nguyenmp.reader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nguyenmp.reddit.data.Link;
import com.nguyenmp.reddit.data.SubredditLinkListing;
import com.nguyenmp.reddit.nio.SubredditLinkListingRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class SubredditLinkListingFragment extends ListFragment
        implements Refreshable,
        LoaderManager.LoaderCallbacks<SubredditLinkListing>{

    /** Specifies the subreddit for this fragment to display the listing of.
     * If not specified, this Fragment will simply show the frontpage. */
    public static final String ARGUMENT_SUBREDDIT = "com.nguyenmp.reader.SubredditLinkListing.ARGUMENT_SUBREDDIT";
    public static final String STATE_SUBREDDIT = "state_subreddit";

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText(getString(R.string.empty_subreddit));
        setListAdapter(new ListAdapter(getActivity()));
        setListShown(false);
        if (savedInstanceState == null) refresh();
        else {
            Bundle args = new Bundle();
            args.putString(ARGUMENT_SUBREDDIT, mSubreddit);
            getLoaderManager().initLoader(0, args, this);
        }
    }

    @Override
    public ListAdapter getListAdapter() {
        return (ListAdapter) super.getListAdapter();
    }

    @Override
    public void refresh() {
        setListShown(false);
        Bundle args = new Bundle();
        args.putString(ARGUMENT_SUBREDDIT, mSubreddit);
        getLoaderManager().restartLoader(0, args, this);
    }

    @Override
    public Loader<SubredditLinkListing> onCreateLoader(int id, Bundle args) {
        return new LinksLoader(getActivity(), args.getString(ARGUMENT_SUBREDDIT));
    }

    @Override
    public void onLoadFinished(Loader<SubredditLinkListing> loader, SubredditLinkListing data) {
        if (data != null) getListAdapter().set(data.getData().getChildren());
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<SubredditLinkListing> loader) {
        getListAdapter().clear();
        setListShown(false);
    }

    private static class LinksLoader extends AsyncTaskLoader<SubredditLinkListing> {
        private final String mSubreddit;
        private SubredditLinkListing mData;

        public LinksLoader(Context context, String subreddit) {
            super(context);
            this.mSubreddit = subreddit;
        }

        @Override
        public SubredditLinkListing loadInBackground() {
            Log.d("Testing", "Loading in background");
            try {
                return new SubredditLinkListingRunnable(mSubreddit).runBlockingMode();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onStartLoading() {
            if (mData != null) {
                // Deliver any previously loaded data immediately.
                deliverResult(mData);
            }

            if (takeContentChanged() || mData == null) {
                // When the observer detects a change, it should call onContentChanged()
                // on the Loader, which will cause the next call to takeContentChanged()
                // to return true. If this is ever the case (or if the current data is
                // null), we force a new load.
                forceLoad();
            }
        }

        @Override
        protected void onStopLoading() {
            // The Loader is in a stopped state, so we should attempt to cancel the
            // current load (if there is one).
            cancelLoad();

            // Note that we leave the observer as is. Loaders in a stopped state
            // should still monitor the data source for changes so that the Loader
            // will know to force a new load if it is ever started again.
        }

        @Override
        public void deliverResult(SubredditLinkListing data) {
            if (isReset()) {
                // The Loader has been reset; ignore the result and invalidate the data.
                releaseResources(data);
                return;
            }

            // Hold a reference to the old data so it doesn't get garbage collected.
            // We must protect it until the new data has been delivered.
            SubredditLinkListing oldData = mData;
            mData = data;

            if (isStarted()) {
                // If the Loader is in a started state, deliver the results to the
                // client. The superclass method does this for us.
                super.deliverResult(data);
            }

            // Invalidate the old data as we don't need it any more.
            if (oldData != null && oldData != data) {
                releaseResources(oldData);
            }
        }


        @Override
        protected void onReset() {
            // Ensure the loader has been stopped.
            onStopLoading();

            // At this point we can release the resources associated with 'mData'.
            if (mData != null) {
                releaseResources(mData);
                mData = null;
            }
        }

        @Override
        public void onCanceled(SubredditLinkListing data) {
            // Attempt to cancel the current asynchronous load.
            super.onCanceled(data);

            // The load has been canceled, so we should release the resources
            // associated with 'data'.
            releaseResources(data);
        }

        private void releaseResources(SubredditLinkListing data) {
            // For a simple List, there is nothing to do. For something like a Cursor, we
            // would close it in this method. All resources associated with the Loader
            // should be released here.
        }
    }

    private static class ListAdapter extends BaseAdapter {
        private final ArrayList<Link> data = new ArrayList<Link>();
        private final Context context;

        private ListAdapter(Context context) {
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
}
