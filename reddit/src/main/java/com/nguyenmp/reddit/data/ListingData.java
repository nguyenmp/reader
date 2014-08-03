package com.nguyenmp.reddit.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize
public class ListingData<ItemType extends Serializable> implements Serializable {
    private String modhash, after, before;
    private ItemType[] children;

    @JsonProperty("modhash")
    public String getModhash() {
        return modhash;
    }

    @JsonProperty("modhash")
    public void setModhash(String modhash) {
        this.modhash = modhash;
    }

    @JsonProperty("after")
    public String getAfter() {
        return after;
    }

    @JsonProperty("after")
    public void setAfter(String after) {
        this.after = after;
    }

    @JsonProperty("before")
    public String getBefore() {
        return before;
    }

    @JsonProperty("before")
    public void setBefore(String before) {
        this.before = before;
    }

    @JsonProperty("children")
    public ItemType[] getChildren() {
        return children;
    }

    @JsonProperty("children")
    public void setChildren(ItemType[] children) {
        this.children = children;
    }
}
