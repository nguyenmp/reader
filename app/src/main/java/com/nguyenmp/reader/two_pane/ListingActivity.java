package com.nguyenmp.reader.two_pane;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.nguyenmp.reader.NavigationDrawerFragment;
import com.nguyenmp.reader.R;

public abstract class ListingActivity<CollectionType> extends BaseActivity implements CollectionManager<CollectionType> {

    private static final String FRAGMENT_TAG_LISTING = "fragment_tag_listing";
    private static final String FRAGMENT_TAG_PAGER = "fragment_tag_pager";

    private int position;
    private CollectionType data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);

        FragmentManager fragmentManager = getSupportFragmentManager();

        final ListFragment<CollectionType> listingFragment;
        if (savedInstanceState == null) {
            listingFragment = getNewListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.links_list_container, listingFragment, FRAGMENT_TAG_LISTING).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.frontpage, menu);
        return true;
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
        ListFragment<CollectionType> listingFragment = getListFragment();
        if (listingFragment != null) {
            listingFragment.setCollection(null);
            listingFragment.setCollectionItem(0);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(FRAGMENT_TAG_PAGER) != null) fm.popBackStack();
        else super.onBackPressed();
    }

    @Override
    public void dispatchSetCollectionItem(int position) {
        this.position = position;
        FragmentManager fm = getSupportFragmentManager();

        // What I want to do is if the pager isn't visible, show it
        PagerFragment<CollectionType> pager = (PagerFragment<CollectionType>) fm.findFragmentById(R.id.comments_pager_container);
        if (pager == null) {
            pager = getNewPagerFragment();
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.frontpage_pager_show, 0, 0, R.anim.frontpage_pager_hide)
                    .addToBackStack(null)
                    .replace(R.id.comments_pager_container, pager, FRAGMENT_TAG_PAGER)
                    .commit();

            // If we need to create the pager, we also need to initialize the data
            pager.setCollection(data);
        }

        // Once we can ensure that the pager is created, we set it's position
        pager.setCollectionItem(position);

        // Do the same for the list
        ListFragment<CollectionType> list = getListFragment();
        if (list != null) list.setCollectionItem(position);
        // TODO: Have a fallback plan for when the list is null
    }

    @Override
    public void dispatchSetCollection(CollectionType data) {
        this.data = data;

        // What I want to do is if the pager isn't visible, show it
        PagerFragment<CollectionType> pager = getPagerFragment();
        if (pager != null) pager.setCollection(data);

        // Do the same for the list
        ListFragment<CollectionType> list = getListFragment();
        if (list != null) list.setCollection(data);
    }

    @Override
    public void dispatchSetLoading(boolean isLoading) {
        ListFragment<CollectionType> listFragment = getListFragment();
        if (listFragment != null) listFragment.setLoading(isLoading);

        PagerFragment<CollectionType> pagerFragment = getPagerFragment();
        if (pagerFragment != null) pagerFragment.setLoading(isLoading);
    }

    @Nullable
    public ListFragment<CollectionType> getListFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return (ListFragment<CollectionType>) fm.findFragmentById(R.id.links_list_container);
    }

    @Nullable
    public PagerFragment<CollectionType> getPagerFragment() {
        FragmentManager fm = getSupportFragmentManager();
        return (PagerFragment<CollectionType>) fm.findFragmentById(R.id.comments_pager_container);
    }

    @NonNull
    public abstract ListFragment<CollectionType> getNewListFragment();

    @NonNull
    public abstract PagerFragment<CollectionType> getNewPagerFragment();
}