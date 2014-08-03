package com.nguyenmp.reddit.data;

public class Comment extends Reply {
    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }

    private CommentData data;
}
