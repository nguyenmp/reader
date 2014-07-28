package com.nguyenmp.reddit.nio;

import com.nguyenmp.reddit.data.LoginData;
import com.nguyenmp.reddit.data.SubredditLinkListing;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.Reader;

public class SubredditLinkListingRunnable extends Get<SubredditLinkListing> {
    private final String subreddit;
    private final String after;

    public SubredditLinkListingRunnable() {
        this(null);
    }

    public SubredditLinkListingRunnable(String subreddit) {
        this(subreddit, (String) null);
    }

    public SubredditLinkListingRunnable(String subreddit, LoginData login) {
        this(subreddit, null, login);
    }

    public SubredditLinkListingRunnable(String subreddit, String after) {
        this(subreddit, after, null);
    }

    public SubredditLinkListingRunnable(String subreddit, String after, LoginData login) {
        super(login);
        this.after = after == null ? "" : after;
        this.subreddit = subreddit;
    }

    @Override
    public String getEndpoint() {
        return subreddit == null ? String.format(".json?after=%s", after) : String.format("r/%s/.json?%s", subreddit, after);
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
