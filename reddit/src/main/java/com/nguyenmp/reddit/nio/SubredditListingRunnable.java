package com.nguyenmp.reddit.nio;

import com.nguyenmp.reddit.data.SubredditListing;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.Reader;

public class SubredditListingRunnable extends Get<SubredditListing> {
    private final int limit;

    public SubredditListingRunnable() {
        this(100);
    }

    public SubredditListingRunnable(int limit) {
        this.limit = limit;
    }

    @Override
    public String getEndpoint() {
        return String.format("subreddits/.json?limit=%d", limit);
    }

    @Override
    public SubredditListing parseResult(Reader reader) throws Exception {
        return new ObjectMapper().readValue(reader, SubredditListing.class);
    }

    @Override
    public void onComplete(SubredditListing result) {
        // Do nothing
    }

    @Override
    public void onError(Exception e) {
        // Do nothing
    }
}
