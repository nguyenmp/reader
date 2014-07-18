package com.nguyenmp.reddit.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by mark on 7/18/14.
 */
public class LoginResult {
    @JsonProperty
    public Json json;

    public static class Json {
        @JsonProperty
        public Float ratelimit;

        @JsonProperty
        public String[][] errors;

        @JsonProperty
        public LoginData data;
    }
}