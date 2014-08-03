package com.nguyenmp.reddit.data;

import com.nguyenmp.reddit.nio.SubredditListingRunnable;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize
public class SubredditListing extends Listing<Subreddit> {
    public static SubredditListing get() throws Exception {
        return new SubredditListingRunnable().runBlockingMode();
    }

    public static SubredditListing get(int count) throws Exception {
        return new SubredditListingRunnable(count).runBlockingMode();
    }
}
