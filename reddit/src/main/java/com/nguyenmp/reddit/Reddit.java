package com.nguyenmp.reddit;

import com.nguyenmp.reddit.data.LoginData;
import com.nguyenmp.reddit.nio.LoginRunnable;

/** An interface to access reddit.com */
public class Reddit {
    private final LoginData login;

    /** An accessor to reddit.com using the default user (no login) */
    public Reddit() {
        this(null);
    }

    /** An accessor to reddit.com using the given cookie as credentials */
    public Reddit(LoginData login) {
        this.login = login;
    }

    /** An accessor to reddit.com using the given credentials */
    public Reddit(String username, String password) throws Exception {
        this.login = new LoginRunnable(username, password).runBlockingMode().json.data;
    }
}
