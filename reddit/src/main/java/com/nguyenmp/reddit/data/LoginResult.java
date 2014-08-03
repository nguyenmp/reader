package com.nguyenmp.reddit.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

public class LoginResult implements Serializable{
    @JsonProperty
    public Json json;

    public static class Json implements Serializable {
        public Float ratelimit;

        public String[][] errors;

        public LoginData data;
    }
}