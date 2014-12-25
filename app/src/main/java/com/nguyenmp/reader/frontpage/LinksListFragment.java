package com.nguyenmp.reader.frontpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;

import com.nguyenmp.reader.R;
import com.nguyenmp.reader.two_pane.LoadableListFragment;
import com.nguyenmp.reddit.data.Link;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

public class LinksListFragment extends LoadableListFragment<Link[]> {

    public static LinksListFragment newInstance() {
        LinksListFragment fragment = new LinksListFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public ListAdapter getNewListAdapter(@NonNull Link[] collection) {
        ImmutableLinksAdapter immutableLinksAdapter = new ImmutableLinksAdapter(collection, getActivity(), (ImmutableLinksAdapter.Callback) getActivity());
        AnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(new AlphaInAnimationAdapter(immutableLinksAdapter));
        animationAdapter.setAbsListView(getListView());
        return animationAdapter;
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
