package com.nguyenmp.reddit.data;

import org.codehaus.jackson.annotate.JsonProperty;

public interface Votable {

    @JsonProperty("ups")
    public int getUps();

    @JsonProperty("ups")
    public void setUps(int ups);

    @JsonProperty("downs")
    public int getDowns();

    @JsonProperty("downs")
    public void setDowns(int downs);

    @JsonProperty("likes")
    public Boolean getLikes();

    @JsonProperty("likes")
    public void setLikes(Boolean likes);

}