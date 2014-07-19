package com.nguyenmp.reddit.data;

public class Listing<ItemType> extends Thing {
    private ListingData<ItemType> data;

    public ListingData<ItemType> getData() {
        return data;
    }

    public void setData(ListingData<ItemType> data) {
        this.data = data;
    }
}
