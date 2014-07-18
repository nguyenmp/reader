package com.nguyenmp.reddit;

import com.nguyenmp.reddit.data.LoginData;
import com.nguyenmp.reddit.nio.LoginRunnable;

/** An interface to access reddit.com */
public class Reddit {

    private Reddit() {
        // Do nothing
    }

    public static LoginData login(String username, String password) throws Exception {
        return new LoginRunnable(username, password).runBlockingMode().json.data;
    }
}
