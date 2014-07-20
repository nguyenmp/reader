package com.nguyenmp.reader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyenmp.reader.data.Account;
import com.nguyenmp.reader.db.AccountsDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements Refreshable {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private static final String STATE_ACCOUNTS_VISIBILITY = "selected_accounts_visibility";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mAccountsListView;
    private ListView mSubredditsListView;
    private TextView mCurrentAccountTextView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        final FragmentActivity context = getActivity();
        final View view = inflater.inflate(R.layout.fragment_navigation_drawer, container);

        // Set up the accounts stuff
        mAccountsListView = (ListView) view.findViewById(R.id.accounts_list_view);
        AccountsAdapter adapter = new AccountsAdapter(context);
        mAccountsListView.setAdapter(adapter);
        mAccountsListView.setOnItemClickListener(new AccountsSelectionListener(context, adapter));
        if (savedState != null) mAccountsListView.setVisibility(savedState.getInt(STATE_ACCOUNTS_VISIBILITY, View.GONE));
        mCurrentAccountTextView = (TextView) view.findViewById(R.id.current_account_text_view);
        final ImageView currentAccountDropdownIndicator = (ImageView) view.findViewById(R.id.accounts_dropdown_indicator);
        mCurrentAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Animate the dropdown list
                boolean isVisible = mAccountsListView.getVisibility() == View.VISIBLE;
                mAccountsListView.setVisibility(isVisible ? View.GONE : View.VISIBLE);

                // Animate the indicator
                final int indicatorAnimationRes = isVisible ? R.anim.rotate_clockwise_180_360 : R.anim.rotate_clockwise_0_180;
                final Animation indicatorAnimation = AnimationUtils.loadAnimation(context, indicatorAnimationRes);
                indicatorAnimation.setFillEnabled(true);
                indicatorAnimation.setFillAfter(true);
                currentAccountDropdownIndicator.startAnimation(indicatorAnimation);
            }
        });

        // Set up the subreddit stuff
        mSubredditsListView = (ListView) view.findViewById(R.id.subreddits_list_view);
        mSubredditsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mSubredditsListView.setAdapter(new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new String[]{
                        getString(R.string.title_section1),
                        getString(R.string.title_section2),
                        getString(R.string.title_section3),
                }
        ));
        mSubredditsListView.setItemChecked(mCurrentSelectedPosition, true);
        return view;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mSubredditsListView != null) {
            mSubredditsListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
        if (mAccountsListView != null) outState.putInt(STATE_ACCOUNTS_VISIBILITY, mAccountsListView.getVisibility());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    private static class AccountsAdapter extends BaseAdapter {
        private final Context context;
        private final List<Account> accounts = new ArrayList<Account>();

        private AccountsAdapter(Context context) {
            this.context = context;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return accounts.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            if (position < accounts.size()) return accounts.get(position);
            else return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // Initialize view if it's not being recycled
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            String username;
            if (position > accounts.size()) {
                username = "Add Account";
            } else if (position == accounts.size()) {
                username = "Not Logged In";
            } else {
                username = accounts.get(position).username;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(username);
            textView.setTextColor(context.getResources().getColorStateList(android.R.color.primary_text_dark));

            return view;
        }

        @Override
        public void notifyDataSetChanged() {
            // Refresh list of accounts from the database
            accounts.clear();
            accounts.addAll(AccountsDatabase.get(context));

            super.notifyDataSetChanged();
        }

        public Account[] getAccounts() {
            return accounts.toArray(new Account[accounts.size()]);
        }
    }

    @Override
    public void refresh() {
        Context context = getActivity();
        if (context == null) return;

        // Refresh current account
        String currentAccount = PreferenceManager.getDefaultSharedPreferences(context).getString("username", "Not Logged In");
        if (mCurrentAccountTextView != null) mCurrentAccountTextView.setText(currentAccount);

        // Refresh accounts
        AccountsAdapter adapter = (AccountsAdapter) mAccountsListView.getAdapter();
        if (adapter != null) adapter.notifyDataSetInvalidated();
    }

    private static class AccountsSelectionListener implements AdapterView.OnItemClickListener {
        private final AccountsAdapter adapter;
        private final FragmentActivity activity;

        private AccountsSelectionListener(FragmentActivity activity, AccountsAdapter adapter) {
            this.adapter = adapter;
            this.activity = activity;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Account[] accounts = adapter.getAccounts();
            // If the item selected is in the list of accounts, use that
            if (position < accounts.length) {
                Account account = accounts[position];
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                prefs.edit()
                        .putString("username", account.username)
                        .putString("cookie", account.data.cookie)
                        .putString("modhash", account.data.modhash)
                        .commit();
            }

            // Otherwise, it's either anonymous
            else if (position == accounts.length) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                prefs.edit()
                        .remove("username")
                        .remove("cookie")
                        .remove("modhash")
                        .commit();
            }

            // Otherwise, it's a login attempt
            else {
                DialogFragment dialog = LoginDialogFragment.newInstance();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                dialog.show(fragmentManager, "Login Dialog");
            }
        }
    }

}
