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
    public void testOAuthConstructor() {
        String oauth_token = null;
        Reddit reddit = new Reddit(oauth_token);
        assertNotNull(reddit);
    }

    @Test
    public void testCredentialsConstructor() {
        String username = null, password = null;
        Reddit reddit = new Reddit(username, password);
        assertNotNull(reddit);
    }
}