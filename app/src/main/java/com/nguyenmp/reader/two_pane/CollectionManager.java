package com.nguyenmp.reader.two_pane;

/**
 * An interface for the manager that synchronizes multiple {@link com.nguyenmp.reader.two_pane.CollectionLister}s.
 * This manager is in charge of making sure all the {@link com.nguyenmp.reader.two_pane.CollectionLister}s are
 * showing the same content at all times (the content as well as the index in that content.
 */
public interface CollectionManager<CollectionType> {

    /** When a lister has changed it's selection index, then it notifies the manager to propagate this down to other listers */
    public void dispatchSetCollectionItem(int position);

    /** called when new data needs to be shown */
    public void dispatchSetCollection(CollectionType data);

    public void dispatchSetLoading(boolean isLoading);

    /** When a lister has reached the end of the content, the lister can query the manager for more data */
    public void loadMore();

    /** called when a view wants to discard all accumulated data for fresh data from the server */
    public void refresh();
}
