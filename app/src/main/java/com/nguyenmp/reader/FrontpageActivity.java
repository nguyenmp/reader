package com.nguyenmp.reader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FrontpageActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, Refreshable, ActionBar.OnNavigationListener {

    private static final String TAG_LISTING_FRAGMENT = "Listing Fragment";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, SubredditLinkListingFragment.newInstance(), TAG_LISTING_FRAGMENT)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayList<String> subreddits = new ArrayList<String>();
        subreddits.add("Frontpage");
        subreddits.add("All");
        subreddits.add("Guilded");
        SubredditsAdapter subredditsAdapter = new SubredditsAdapter(this, subreddits);
        actionBar.setListNavigationCallbacks(subredditsAdapter, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.frontpage, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refresh() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Refresh the drawer
        NavigationDrawerFragment drawerFragment =
                (NavigationDrawerFragment) fragmentManager.findFragmentById(R.id.navigation_drawer);
        drawerFragment.refresh();

        // Refresh subreddit viewing
        SubredditLinkListingFragment listingFragment =
                (SubredditLinkListingFragment) fragmentManager.findFragmentByTag(TAG_LISTING_FRAGMENT);
        listingFragment.refresh();
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SubredditLinkListingFragment listingFragment =
                (SubredditLinkListingFragment) fragmentManager.findFragmentByTag(TAG_LISTING_FRAGMENT);

        // TODO: Swap listing for new subreddit

        return true;
    }

    private static class SubredditsAdapter extends ArrayAdapter<String> {

        public SubredditsAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_spinner_dropdown_item, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setTextColor(view.getContext().getResources().getColor(android.R.color.white));

            return view;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setTextColor(view.getContext().getResources().getColor(android.R.color.white));

            return view;
        }
    }
}
