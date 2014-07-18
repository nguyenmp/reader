package com.nguyenmp.reddit;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RedditTest {

    @Test
    public void testNoArgsConstructor() {
        Reddit reddit = new Reddit();
        assertNotNull(reddit);
    }

    @Test
    public void testReconstructor() {
        Reddit reddit = new Reddit(null);
        assertNotNull(reddit);
    }

    @Test
    public void testCredentialsConstructor() throws Exception {
        String username = "", password = "";
        Reddit reddit = new Reddit(username, password);
        assertNotNull(reddit);
    }

    @Test
    public void testBadCredentialsConstructor() throws Exception {
        String username = "", password = "";
        Reddit reddit = new Reddit(username, password);
        assertNotNull(reddit);
    }
}