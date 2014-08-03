package com.nguyenmp.reddit.data;

import java.io.Serializable;

public class Link extends Thing implements Serializable {
    private LinkData data;

    public LinkData getData() {
        return data;
    }

    public void setData(LinkData data) {
        this.data = data;
    }
}
