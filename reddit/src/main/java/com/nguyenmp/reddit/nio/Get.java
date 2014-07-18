package com.nguyenmp.reddit.nio;

import com.nguyenmp.reddit.NotImplementedException;

import java.net.HttpURLConnection;

public abstract class Get extends Connection {

    @Override
    public void initializeConnection(HttpURLConnection connection) {
        throw new NotImplementedException();
    }
}
