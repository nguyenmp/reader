package com.nguyenmp.reader.two_pane;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nguyenmp.reader.R;

import java.io.Serializable;

public abstract class ListFragment<CollectionType> extends Fragment implements CollectionLister<CollectionType> {
    // References to our views that are generated after we
    // create a view and destroyed after we destroy the view
    private ListView list_view;
    private ProgressBar progress_bar;
    private TextView empty_text;

    // Keys to the state bundle (we store the position and the content in the bundles)
    private static final String STATE_POSITION = "state_position";
    private static final String STATE_DATA = "state_data";

    // Helper variables that store state if view hasn't been created yet
    // When view is created, these variables will be used to initialize
    private CollectionType data = null;
    private Integer position = null;

    @Override
    public void onCreate(Bundle inState) {
        super.onCreate(inState);

        if (inState != null) {
            if (inState.containsKey(STATE_DATA)) data = (CollectionType) inState.getSerializable(STATE_DATA);
            if (inState.containsKey(STATE_POSITION)) position = inState.getInt(STATE_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Generate the view
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Extract member variables from the generated view
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        empty_text = (TextView) view.findViewById(R.id.empty_text);
        list_view = (ListView) view.findViewById(R.id.list_view);

        // If data was already initialized (i.e. set at construction or
        // from previous state) create a new adapter to show that data
        setCollection(data);

        // If the position was already initialized from construction
        // or previous state, set that as our position
        if (position != null) setCollectionItem(position);
        else setCollectionItem(0);

        // Return the generated view
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progress_bar = null;
        empty_text = null;
        list_view = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Store our pager's state (i.e. the data and the item we are viewing)
        if (data != null) outState.putSerializable(STATE_DATA, (Serializable) data);
        if (position != null) outState.putInt(STATE_POSITION, position);
    }

    @Override
    public void setCollectionItem(int position) {
        this.position = position;
        if (list_view != null) {
            list_view.setSelection(position);
        }
    }

    @Override
    public void setCollection(@Nullable CollectionType collection) {
        this.data = collection;
        if (list_view != null) {
            if (collection == null) {
                list_view.setVisibility(View.GONE);
                empty_text.setVisibility(View.GONE);
                progress_bar.setVisibility(View.VISIBLE);
            } else if (isCollectionEmpty(collection)) {
                list_view.setVisibility(View.GONE);
                progress_bar.setVisibility(View.GONE);
                empty_text.setVisibility(View.VISIBLE);
                empty_text.setText(getEmptyText());
            } else {
                progress_bar.setVisibility(View.GONE);
                empty_text.setVisibility(View.GONE);
                list_view.setVisibility(View.VISIBLE);
                list_view.setAdapter(wrapListAdapter(getNewListAdapter(collection)));
            }
        }
    }

    @NonNull
    public ListAdapter wrapListAdapter(ListAdapter originalListAdapter) {
        return originalListAdapter;
    }

    @NonNull
    public abstract ListAdapter getNewListAdapter(@NonNull CollectionType collection);

    public abstract boolean isCollectionEmpty(@NonNull CollectionType collection);

    @Nullable
    public abstract String getEmptyText();

    @Override
    public void setLoading(boolean isLoading) {
        // Ignored -- this is handled by LoadableListFragment
    }

    @Nullable
    public ListView getListView() {
        return list_view;
    }
}