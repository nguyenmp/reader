package com.nguyenmp.reader.adapters;


import android.util.Log;
import android.widget.BaseAdapter;

import com.nguyenmp.reader.Refreshable;

public abstract class ChildAdapter extends BaseAdapter implements Refreshable {
    private MergedAdapter parent;

    public void setParent(MergedAdapter parent) {
        this.parent = parent;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        // Propagate data set changes to the parent
        if (parent != null) parent.notifyDataSetChanged();
        else Log.w("ChildAdapter", "No parent set for child adapter.");
    }
}