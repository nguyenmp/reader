package com.nguyenmp.reddit.nio;

import com.nguyenmp.reddit.data.LoginData;

import java.net.HttpURLConnection;

public abstract class Get<ResultType> extends Connection<ResultType> {
    private final LoginData login;

    public Get() {
        this.login = null;
    }

    public Get(LoginData login) {
        this.login = login;
    }

    @Override
    public void initializeConnection(HttpURLConnection connection) {
        if (login != null) connection.setRequestProperty("Cookie:", login.cookie);
    }
}
