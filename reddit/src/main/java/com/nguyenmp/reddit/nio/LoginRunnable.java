package com.nguyenmp.reddit.nio;

import com.nguyenmp.reddit.data.LoginResult;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.Reader;

public class LoginRunnable extends Post<LoginResult> {
    private final String username, password;

    public LoginRunnable(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Query getQuery() {
        Query query = new Query();

        query.add(new NameValuePair("api_type", "json"));
        query.add(new NameValuePair("passwd", password));
        query.add(new NameValuePair("rem", "true"));
        query.add(new NameValuePair("user", username));

        return query;
    }

    @Override
    public String getEndpoint() {
        return "api/login";
    }

    @Override
    public LoginResult parseResult(Reader reader) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = asString(reader);
        return mapper.readValue(json, LoginResult.class);
    }

    @Override
    public void onComplete(LoginResult result) {
        // Do nothing
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
