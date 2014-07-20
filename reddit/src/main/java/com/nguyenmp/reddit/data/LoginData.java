package com.nguyenmp.reddit.data;

import org.codehaus.jackson.annotate.JsonProperty;

public class LoginData {
    @JsonProperty
    public String modhash, cookie;

    public LoginData() {
        // Do nothing (this is required for Jackson)
    }

    public LoginData(String cookie, String modhash) {
        this.cookie = cookie;
        this.modhash = modhash;
    }

    public LoginData(LoginData other) {
        this.modhash = other.modhash;
        this.cookie = other.cookie;
    }

    @Override
    public LoginData clone() {
        return new LoginData(this);
    }
}