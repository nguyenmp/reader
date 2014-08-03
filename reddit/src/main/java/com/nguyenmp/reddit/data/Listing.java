package com.nguyenmp.reddit.data;

import java.io.Serializable;

public class Listing<ItemType extends Serializable> extends Thing implements Serializable {
    private ListingData<ItemType> data;

    public ListingData<ItemType> getData() {
        return data;
    }

    public void setData(ListingData<ItemType> data) {
        this.data = data;
    }
}
