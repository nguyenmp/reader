package com.nguyenmp.reddit;

import com.nguyenmp.reddit.nio.Post;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Reader;

/** An interface to access reddit.com */
public class Reddit {
    private final LoginResult.Json.LoginData login;

    /** An accessor to reddit.com using the default user (no login) */
    public Reddit() {
        this(null);
    }

    /** An accessor to reddit.com using the given cookie as credentials */
    public Reddit(LoginResult.Json.LoginData login) {
        this.login = login;
    }

    /** An accessor to reddit.com using the given credentials */
    public Reddit(String username, String password) throws Exception {
        this.login = new LoginRunnable(username, password).runBlockingMode().json.data;

    }

    private static class LoginRunnable extends Post<LoginResult> {
        private final String username, password;

        private LoginRunnable(String username, String password) {
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

    public static class LoginResult {
        @JsonProperty
        Json json;

        private static class Json {
            @JsonProperty
            Float ratelimit;

            @JsonProperty
            String[][] errors;

            @JsonProperty
            LoginData data;


            private static class LoginData {
                @JsonProperty
                String modhash, cookie;
            }
        }
    }
}
