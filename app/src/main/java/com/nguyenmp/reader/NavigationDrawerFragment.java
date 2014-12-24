package com.nguyenmp.reader;

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
import android.support.v7.widget.PopupMenu;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nguyenmp.reader.adapters.ChildAdapter;
import com.nguyenmp.reader.data.Account;
import com.nguyenmp.reader.db.AccountsDatabase;
import com.nguyenmp.reader.util.SerialHelper;
import com.nguyenmp.reddit.CookieSession;
import com.nguyenmp.reddit.data.LoginData;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements Refreshable {
    public static final String FRAGMENT_TAG = "navigation_drawer";

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
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
        final View view = inflater.inflate(R.layout.fragment_navigation_drawer, container);

        // Set up the accounts stuff
        final Account[] accounts = AccountsDatabase.get(view.getContext());
        final ImageView currentAccountDropdownIndicator = (ImageView) view.findViewById(R.id.accounts_dropdown_indicator);
        currentAccountDropdownIndicator.setOnClickListener(new ShowAccountsOnClickListener(getActivity(), accounts));
        mCurrentAccountTextView = (TextView) view.findViewById(R.id.current_account_text_view);

        // If we are logged in, clicking the username will shoot you to the profile
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        if (getCurrentAccount(view.getContext()) != null) refresh();

        // If we are not logged in, clicking the username will prompt accounts
        else mCurrentAccountTextView.setOnClickListener(new ShowAccountsOnClickListener(getActivity(), currentAccountDropdownIndicator, accounts));

        // Set up the subreddit stuff
        mSubredditsListView = (ListView) view.findViewById(R.id.subreddits_list_view);
        mSubredditsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mSubredditsListView.setAdapter(new ArrayAdapter<>(
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
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

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void refresh() {
        Context context = getActivity();
        if (context == null) return;

        // Refresh current account
        String currentAccount = PreferenceManager.getDefaultSharedPreferences(context).getString("username", "Not Logged In");
        if (mCurrentAccountTextView != null) mCurrentAccountTextView.setText(currentAccount);
    }

    public static Account getCurrentAccount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String username = prefs.getString("username", null);
        String loginDataString = prefs.getString("login_data", null);

        if (username != null && loginDataString != null) {
            LoginData loginData = (LoginData) SerialHelper.fromString(loginDataString);
            CookieSession cookieSession = new CookieSession(loginData);
            return new Account(username, cookieSession);
        } else {
            return null;
        }
    }

    public static void setCurrentAccount(Context context, Account account) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Null account means unset all account properties
        if (account == null) {
            prefs.edit()
                    .remove("username")
                    .remove("login_data")
                    .apply();
        } else {
            prefs.edit()
                    .putString("username", account.username)
                    .putString("login_data", SerialHelper.toString(account.data))
                    .apply();
        }
    }

    /** Shows a list of accounts/actions and animates the views */
    private static class ShowAccountsOnClickListener implements View.OnClickListener {
        private final FragmentActivity fragmentActivity;
        private final View dropdownIndicator;
        private final Account[] accounts;

        /** Creates a listener that will animate the clicked view */
        private ShowAccountsOnClickListener(FragmentActivity fragmentActivity, Account[] accounts) {
            this.fragmentActivity = fragmentActivity;
            this.accounts = accounts;
            this.dropdownIndicator = null;
        }

        /** Creates a listener that will animate the given view */
        private ShowAccountsOnClickListener(FragmentActivity fragmentActivity, View dropdownIndicator, Account[] accounts) {
            this.fragmentActivity = fragmentActivity;
            this.dropdownIndicator = dropdownIndicator;
            this.accounts = accounts;
        }

        @Override
        public void onClick(View v) {
            // Show the dropdown list
            PopupMenu accountsMenu = new PopupMenu(v.getContext(), v);
            Menu menu = accountsMenu.getMenu();
            int order = 0;
            for (Account account : accounts) menu.add(Menu.NONE, Menu.NONE, order++, account.username);
            menu.add(Menu.NONE, Menu.NONE, order++, "Anonymous");
            menu.add(Menu.NONE, Menu.NONE, order, "Add An Account");
            accountsMenu.show();
            accountsMenu.setOnMenuItemClickListener(new AccountsSelectionListener(v.getContext(), fragmentActivity, accounts));

            // Animate the indicator
            View target = dropdownIndicator != null ? dropdownIndicator : v;
            final int indicatorAnimationRes = R.anim.grow;
            final Animation indicatorAnimation = AnimationUtils.loadAnimation(v.getContext(), indicatorAnimationRes);
            target.startAnimation(indicatorAnimation);
        }
    }

    private static class AccountsSelectionListener implements PopupMenu.OnMenuItemClickListener {
        private final Context context;
        private final FragmentActivity fragmentActivity;
        private final Account[] accounts;

        private AccountsSelectionListener(Context context, FragmentActivity fragmentActivity, Account[] accounts) {
            this.context = context;
            this.fragmentActivity = fragmentActivity;
            this.accounts = accounts;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int position = menuItem.getOrder();

            // If the item selected is in the list of accounts, use that
            if (position < accounts.length) {
                Account account = accounts[position];
                setCurrentAccount(context, account);
            }

            // Otherwise, it's either anonymous
            else if (position == accounts.length) {
                setCurrentAccount(context, null);
            }

            // Otherwise, it's a login attempt
            else {
                DialogFragment dialog = LoginDialogFragment.newInstance();
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                dialog.show(fragmentManager, "Login Dialog");
            }

            // Refresh and say we handled this
            ((Refreshable) fragmentActivity).refresh();
            return true;
        }
    }

    private static class SettingsAdapter extends ChildAdapter {
        // TODO: Implement SettingsAdapter
        private final Context mContext;

        private SettingsAdapter(Context context, boolean loggedIn) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public void refresh() {

        }
    }
}
