package com.nguyenmp.reader.adapters;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nguyenmp.reader.Refreshable;

public class MergedAdapter extends BaseAdapter implements Refreshable {
    private final ChildAdapter[] children;

    private MergedAdapter(ChildAdapter... children) {
        // Bind to the given child adapters
        this.children = children;

        // Set ourself to be the parent of these adapters so we get notified when
        // their individual data set information gets changed
        for (ChildAdapter child : children) child.setParent(this);
    }

    @Override
    public int getCount() {
        int sum = 0;
        for (ChildAdapter child : children) sum += child.getCount();
        return sum;
    }

    @Override
    public Object getItem(int position) {
        for (ChildAdapter child : children) {
            if (position < child.getCount()) return child.getItem(position);
            else position += child.getCount();
        }

        throw new IndexOutOfBoundsException("No child view at position: " + position);
    }

    @Override
    public long getItemId(int position) {
        for (ChildAdapter child : children) {
            if (position < child.getCount()) return child.getItemId(position);
            else position += child.getCount();
        }

        throw new IndexOutOfBoundsException("No child view at position: " + position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        for (ChildAdapter child : children) {
            if (position < child.getCount()) return child.getView(position, view, parent);
            else position += child.getCount();
        }

        throw new IndexOutOfBoundsException("No child view at position: " + position);
    }

    @Override
    public void refresh() {
        for (ChildAdapter child : children) child.refresh();
    }
}