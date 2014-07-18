package com.nguyenmp.reddit.data;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by mark on 7/18/14.
 */
public class LoginData {
    @JsonProperty
    public String modhash, cookie;
}