package com.nguyenmp.reader.frontpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.nguyenmp.reader.loaders.SubredditLinksLoader;
import com.nguyenmp.reader.two_pane.ListFragment;
import com.nguyenmp.reader.two_pane.ListingActivity;
import com.nguyenmp.reader.two_pane.PagerFragment;
import com.nguyenmp.reddit.data.Link;
import com.nguyenmp.reddit.data.SubredditLinkListing;

public class FrontpageActivity extends ListingActivity<Link[]>
        implements LoaderManager.LoaderCallbacks<SubredditLinkListing> {

    private static final int LOADER_ID = 0;

    @NonNull
    @Override
    public ListFragment<Link[]> getNewListFragment() {
        return LinksListFragment.newInstance();
    }

    @NonNull
    @Override
    public PagerFragment<Link[]> getNewPagerFragment() {
        return LinksPagerFragment.newInstance();
    }

    @Override
    public void loadMore() {
        getSupportLoaderManager().getLoader(LOADER_ID).onContentChanged();
        dispatchSetLoading(true);
    }

    @Override
    public Loader<SubredditLinkListing> onCreateLoader(int id, Bundle args) {
        dispatchSetLoading(true);
        return new SubredditLinksLoader(this, null);
    }

    @Override
    public void onLoadFinished(Loader<SubredditLinkListing> loader, SubredditLinkListing data) {
        dispatchSetCollection(data.getData().getChildren());
        dispatchSetLoading(false);
    }

    @Override
    public void onLoaderReset(Loader<SubredditLinkListing> loader) {
        dispatchSetLoading(true);
    }
}
