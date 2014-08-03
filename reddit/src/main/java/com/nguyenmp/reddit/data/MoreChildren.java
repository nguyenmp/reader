package com.nguyenmp.reddit.data;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@JsonSerialize
public class MoreChildren extends Reply {
    public MoreChildrenData getData() {
        return data;
    }

    public void setData(MoreChildrenData data) {
        this.data = data;
    }

    private MoreChildrenData data;
}
