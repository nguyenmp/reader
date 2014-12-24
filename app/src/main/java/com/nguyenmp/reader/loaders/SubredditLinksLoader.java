package com.nguyenmp.reader.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.nguyenmp.reddit.data.Link;
import com.nguyenmp.reddit.data.SubredditLinkListing;
import com.nguyenmp.reddit.nio.SubredditLinkListingRunnable;

public class SubredditLinksLoader extends AsyncTaskLoader<SubredditLinkListing> {
    private final String mSubreddit;
    private SubredditLinkListing mData;

    public SubredditLinksLoader(Context context, String subreddit) {
        super(context);
        this.mSubreddit = subreddit;
    }

    @Override
    public SubredditLinkListing loadInBackground() {
        try {
            String after = mData == null ? null : mData.getData().getAfter();
            return new SubredditLinkListingRunnable.Builder()
                    .forSubreddit(mSubreddit)
                    .after(after)
                    .build()
                    .call();
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
        if (oldData != null) {
            Link[] oldChildren = oldData.getData().getChildren();
            Link[] freshChildren = mData.getData().getChildren();
            Link[] newChildren = new Link[oldChildren.length + freshChildren.length];
            System.arraycopy(oldChildren, 0, newChildren, 0, oldChildren.length);
            System.arraycopy(freshChildren, 0, newChildren, oldChildren.length, freshChildren.length);
            mData.getData().setChildren(newChildren);
        }

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
