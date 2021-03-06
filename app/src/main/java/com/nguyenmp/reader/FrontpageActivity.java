package com.nguyenmp.reader;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nguyenmp.reader.dialogs.SubredditPickerDialog;
import com.nguyenmp.reddit.data.Link;

public class FrontpageActivity extends ActionBarActivity
        implements Refreshable,
        SubredditPickerDialog.Callback,
        LinksListFragment.Callback {

    private static final String FRAGMENT_TAG_SUBREDDIT_LISTING = "Listing Fragment";
    private static final String FRAGMENT_TAG_PAGER = "Post";
    private static final String STATE_SELECTED_SUBREDDIT = "state_selected_subreddit";

    private String mSubreddit = null;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);

        FragmentManager fragmentManager = getSupportFragmentManager();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                fragmentManager.findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                drawerLayout);

        final LinksListFragment listingFragment;
        if (savedInstanceState == null) {
            listingFragment = LinksListFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.links_list_container, listingFragment, FRAGMENT_TAG_SUBREDDIT_LISTING).commit();
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle mDrawerToggle= new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STATE_SELECTED_SUBREDDIT, mSubreddit);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);

        mSubreddit = inState.getString(STATE_SELECTED_SUBREDDIT);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.subreddit_picker_view);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        TextView titleView = (TextView) actionBar.getCustomView();
        titleView.setText(mSubreddit == null ? getString(R.string.frontpage) : mSubreddit);
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                SubredditPickerDialog.newInstance().show(fm, "SubredditPicker");
            }
        });
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
                (NavigationDrawerFragment) fragmentManager.findFragmentByTag(NavigationDrawerFragment.FRAGMENT_TAG);
        drawerFragment.refresh();

        // Refresh subreddit viewing
        LinksListFragment listingFragment =
                (LinksListFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_SUBREDDIT_LISTING);
        if (listingFragment != null) listingFragment.refresh();
    }

    @Override
    public void onSubredditPicked(String subreddit) {
        // If both past and future are null or equal, ignore the picked request
        if (subreddit == null) {
            if (mSubreddit == null) return;
        } else if (subreddit.equals(mSubreddit)) return;

        // Otherwise, the picked subreddit is new and different
        mSubreddit = subreddit;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Remove the fragment that shows the post
        CommentsFragment postFragment = (CommentsFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_PAGER);
        if (postFragment != null) fragmentTransaction.remove(postFragment);

        // Refresh subreddit viewing
        LinksListFragment subredditFragment = (LinksListFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_SUBREDDIT_LISTING);
        if (subredditFragment == null) {
            subredditFragment = LinksListFragment.newInstance(subreddit);
            fragmentTransaction.replace(R.id.links_list_container, subredditFragment, FRAGMENT_TAG_SUBREDDIT_LISTING);
        }
        subredditFragment.setSubreddit(subreddit);
        subredditFragment.refresh();

        // Commit changes
        fragmentTransaction.commit();

        // Update actionbar for new subreddit
        restoreActionBar();
    }

    @Override
    public void onLinkClicked(Link[] links, int position) {
        FragmentManager fm = getSupportFragmentManager();

        // What I want to do is if the pager isn't visible, show it
        LinksPagerFragment pager = (LinksPagerFragment) fm.findFragmentById(R.id.comments_pager_container);
        if (pager == null) {
            pager = LinksPagerFragment.newInstance();
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.frontpage_pager_show, 0, 0, R.anim.frontpage_pager_hide)
                    .addToBackStack(null)
                    .replace(R.id.comments_pager_container, pager, FRAGMENT_TAG_PAGER)
                    .commit();

            LinksListFragment list = (LinksListFragment) fm.findFragmentById(R.id.links_list_container);
            pager.setCallback(list);
        }

        // Update the pager list if needed
        pager.setItems(links);

        // Then select the item
        pager.setCurrentItem(position, true);
    }

    @Override
    public void onMoreLoaded(Link[] links) {
        // Show the new items
        FragmentManager fm = getSupportFragmentManager();
        LinksPagerFragment pager = (LinksPagerFragment) fm.findFragmentById(R.id.comments_pager_container);
        if (pager != null) {
            pager.setItems(links);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(FRAGMENT_TAG_PAGER) != null) fm.popBackStack();
        else super.onBackPressed();
    }
}
