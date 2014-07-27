package com.nguyenmp.reader;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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

public class SubredditLinkListingFragment extends ListFragment implements Refreshable {

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
        setListAdapter(new ListAdapter(getActivity()));
        Log.d("Subreddits", "Saved state = " + savedInstanceState);
        refresh();
    }

    @Override
    public ListAdapter getListAdapter() {
        return (ListAdapter) super.getListAdapter();
    }

    @Override
    public void refresh() {
        getListAdapter().clear();
        setListShown(false);
        Activity activity = getActivity();
        if (activity != null) {
            new FetchListingTask(mSubreddit, getListAdapter(), this).execute();
        }
    }

    private static class FetchListingTask extends AsyncTask<Void, Void, SubredditLinkListing> {
        private final String subreddit;
        private final ListAdapter adapter;
        private final ListFragment fragment;

        private FetchListingTask(String subreddit, ListAdapter adapter, ListFragment fragment) {
            this.subreddit = subreddit;
            this.adapter = adapter;
            this.fragment = fragment;
        }

        @Override
        protected SubredditLinkListing doInBackground(Void... params) {
            try {
                return new SubredditLinkListingRunnable(subreddit).runBlockingMode();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(SubredditLinkListing subredditLinkListing) {
            if (subredditLinkListing != null) {
                adapter.add(subredditLinkListing.getData().getChildren());
                fragment.setListShown(true);
            }
        }
    }

    private static class ListAdapter extends BaseAdapter {
        private static final int TYPE_SELF_POST = 0, TYPE_LINK = 1;
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
        public int getItemViewType(int position) {
            return data.get(position).getData().isIs_self() ? TYPE_SELF_POST : TYPE_LINK;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // Inflate a new view if we cannot recycle an old one
            if (view == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                view = layoutInflater.inflate(R.layout.list_item_link, parent, false);
            }

            Link link = getItem(position);

            TextView text1 = (TextView) view.findViewById(R.id.list_item_link_title);
            text1.setText(link.getData().getTitle());

            TextView text2 = (TextView) view.findViewById(R.id.list_item_link_subtitle);
            text2.setText(link.getData().getSubreddit());

            return view;
        }

        public void add(Link... newData) {
            data.addAll(Arrays.asList(newData));
            notifyDataSetChanged();
        }

        public void clear() {
            data.clear();
            notifyDataSetChanged();
        }
    }
}
