package com.nguyenmp.reddit.data;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize
public class Comment extends Reply {
    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }

    private CommentData data;
}
