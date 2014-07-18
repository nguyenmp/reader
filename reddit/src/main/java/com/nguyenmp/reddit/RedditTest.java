package com.nguyenmp.reddit;

import com.nguyenmp.reddit.data.LoginData;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class RedditTest {

    @Test
    public void testLogin() throws Exception {
        String username = "asdf", password = "fdsa";
        LoginData login = Reddit.login(username, password);
        assertNull(login);
    }

}