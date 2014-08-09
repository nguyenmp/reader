package com.nguyenmp.reader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenmp.reader.adapters.LinksAdapter;
import com.nguyenmp.reddit.data.Link;

public class LinksPagerFragment extends Fragment implements LinksAdapter.Callback {
    private LinksAdapter.Callback callback;
    private ViewPager pager;
    private SubredditLinksPagerAdapter adapter;

    private static final String STATE_POSITION = "state_position";
    private static final String STATE_LINKS = "state_links";

    // Helper variables that store state if view hasn't been created yet
    // When view is created, these variables will be used to initialize
    private Link[] links = null;
    private Integer position = null;

    @Override
    public void onCreate(Bundle inState) {
        super.onCreate(inState);

        if (inState != null) {
            if (inState.containsKey(STATE_LINKS)) {
                links = (Link[]) inState.getSerializable(STATE_LINKS);
            }

            if (inState.containsKey(STATE_POSITION)) {
                position = inState.getInt(STATE_POSITION);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        adapter = new SubredditLinksPagerAdapter(getFragmentManager(), this);
        if (links != null) {
            adapter.set(links);
            links = null;
        }
        pager = (ViewPager) view.findViewById(R.id.view_pager);
        pager.setAdapter(adapter);
        if (position != null) {
            pager.setCurrentItem(position, true);
            position = null;
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (links != null) outState.putSerializable(STATE_LINKS, links);
        else if (adapter != null) {
            links = adapter.get();
            outState.putSerializable(STATE_LINKS, links);
        }

        if (position != null) outState.putInt(STATE_POSITION, position);
        else if (pager != null) {
            outState.putInt(STATE_POSITION, pager.getCurrentItem());
        }
    }

    public static LinksPagerFragment newInstance() {
        return new LinksPagerFragment();
    }

    public void setItems(Link[] links) {
        if (adapter != null) adapter.set(links);
        else this.links = links;
    }

    public void setCurrentItem(int position, boolean smoothScroll) {
        if (pager != null) pager.setCurrentItem(position, smoothScroll);
        else this.position = position;
    }

    @Override
    public void loadMore() {
        if (callback != null) callback.loadMore();
    }

    public void setCallback(LinksAdapter.Callback callback) {
        this.callback = callback;
    }
}
