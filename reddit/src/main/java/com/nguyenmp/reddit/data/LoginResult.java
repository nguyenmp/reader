package com.nguyenmp.reddit.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class LoginResult {
    @JsonProperty
    public Json json;

    public static class Json {
        public Float ratelimit;

        public String[][] errors;

        public LoginData data;
    }
}