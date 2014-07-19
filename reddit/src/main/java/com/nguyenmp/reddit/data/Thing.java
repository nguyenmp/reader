package com.nguyenmp.reddit.data;

import org.codehaus.jackson.annotate.JsonProperty;

public class Thing {
    private String kind;

    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    @JsonProperty("kind")
    public void setKind(String kind) {
        this.kind = kind;
    }
}
