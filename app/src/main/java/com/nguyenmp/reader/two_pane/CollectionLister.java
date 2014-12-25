package com.nguyenmp.reader.two_pane;

import android.support.annotation.Nullable;

/**
 * Shows either the collection of data or an element in that collection of data in context of the collection.
 * @param <CollectionType> the type of data to synchronize with everyone else.
 */
public interface CollectionLister<CollectionType> {

    /**
     * Sets which item in the collection to focus on.
     * Implementations should ignore this call if they are already set to a position.
     * Implementations should also call {@link com.nguyenmp.reader.two_pane.CollectionManager#dispatchSetCollectionItem(int)}
     * in the event that this position is new.
     * @param position the position in the collection to focus on.
     */
    public void setCollectionItem(int position);

    /**
     * Sets the actual collection to be showing.  Null for a spinner, and an empty collection for empty text.
     * @param collection the collection of data to show, or null for a spinner.
     */
    public void setCollection(@Nullable CollectionType collection);

    /** Called by CollectionManager when data started loading or has stopped loading.  Used to show progress indicators. */
    public void setLoading(boolean isLoading);
}