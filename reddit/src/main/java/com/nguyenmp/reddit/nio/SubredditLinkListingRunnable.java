package com.nguyenmp.reddit.nio;

import com.nguyenmp.reddit.data.LoginData;
import com.nguyenmp.reddit.data.SubredditLinkListing;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.Reader;

public class SubredditLinkListingRunnable extends Get<SubredditLinkListing> {
    private final String subreddit;

    public SubredditLinkListingRunnable() {
        this(null, null);
    }

    public SubredditLinkListingRunnable(String subreddit) {
        this(subreddit, null);
    }

    public SubredditLinkListingRunnable(String subreddit, LoginData login) {
        super(login);
        this.subreddit = subreddit;
    }

    @Override
    public String getEndpoint() {
        return subreddit == null ? ".json" : String.format("r/%s/.json", subreddit);
    }

    @Override
    public SubredditLinkListing parseResult(Reader reader) throws Exception {
        return new ObjectMapper().readValue(reader, SubredditLinkListing.class);
    }

    @Override
    public void onComplete(SubredditLinkListing result) {
        System.out.println("SubredditListing: ");
    }

    @Override
    public void onError(Exception e) {

    }
}
