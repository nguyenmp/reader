package com.nguyenmp.reddit.data.test;

import com.nguyenmp.reddit.data.ListingData;
import com.nguyenmp.reddit.data.Subreddit;
import com.nguyenmp.reddit.data.SubredditListing;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SubredditListingTest {

    @Test
    public void test() throws Exception {
        SubredditListing listing = SubredditListing.get();
        assertNotNull(listing);
        assertEquals(listing.getKind(), "Listing");

        ListingData<Subreddit> listingData = listing.getData();
        assertNotNull(listingData);
        assertNull(listingData.getBefore());
        assertNotNull(listingData.getAfter());
        assertNotNull(listingData.getModhash());
        assertEquals(listingData.getModhash(), "");
    }
}