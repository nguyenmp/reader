package com.nguyenmp.reddit.data.test;

import com.nguyenmp.reddit.data.Comments;

import org.junit.Test;

public class CommentsTest {

    @Test
    public void testGet() throws Exception {
        Comments comments = Comments.get("2c449y");
        System.out.println(comments);
    }
}