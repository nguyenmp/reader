package com.nguyenmp.reddit.data;

public class Link extends Thing {
    private LinkData data;

    public LinkData getData() {
        return data;
    }

    public void setData(LinkData data) {
        this.data = data;
    }
}
