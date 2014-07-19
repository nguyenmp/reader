package com.nguyenmp.reader;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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

    public static SubredditLinkListingFragment newInstance() {
        return new SubredditLinkListingFragment();
    }

    public static SubredditLinkListingFragment newInstance(String subreddit) {
        SubredditLinkListingFragment fragment = new SubredditLinkListingFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_SUBREDDIT, subreddit);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(new ListAdapter(getActivity()));
        refresh();
    }

    @Override
    public void refresh() {
        Activity activity = getActivity();
        if (activity != null) {
            new FetchListingTask(null, (ListAdapter) getListAdapter()).execute();
        }
    }

    private static class FetchListingTask extends AsyncTask<Void, Void, SubredditLinkListing> {
        private final String subreddit;
        private final ListAdapter adapter;

        private FetchListingTask(String subreddit, ListAdapter adapter) {
            this.subreddit = subreddit;
            this.adapter = adapter;
        }

        @Override
        protected SubredditLinkListing doInBackground(Void... params) {
            try {
                SubredditLinkListing result =  new SubredditLinkListingRunnable(subreddit).runBlockingMode();
                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(SubredditLinkListing subredditLinkListing) {
            if (subredditLinkListing != null) {
                adapter.add(subredditLinkListing.getData().getChildren());
            }
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
    }
}
