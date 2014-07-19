package com.nguyenmp.reddit.data.test;

import com.nguyenmp.reddit.Reddit;
import com.nguyenmp.reddit.data.Link;
import com.nguyenmp.reddit.data.ListingData;
import com.nguyenmp.reddit.data.LoginData;
import com.nguyenmp.reddit.data.SubredditLinkListing;

import org.junit.Test;

import static org.junit.Assert.*;

public class SubredditLinkListingTest {

    @Test
    public void test() throws Exception {
        SubredditLinkListing listing = SubredditLinkListing.get();
        assertNotNull(listing);
        assertEquals(listing.getKind(), "Listing");

        ListingData<Link> listingData = listing.getData();
        assertNotNull(listingData);
        assertNull(listingData.getBefore());
        assertNotNull(listingData.getModhash());
        assertEquals(listingData.getModhash(), "");
    }

    @Test
    public void test2() throws Exception {
        LoginData login = Reddit.login("markerz", "SHP39E1LLR87");
        SubredditLinkListing listing = SubredditLinkListing.get(login);
        assertNotNull(listing);
        assertEquals(listing.getKind(), "Listing");

        ListingData<Link> listingData = listing.getData();
        assertNotNull(listingData);
        assertNull(listingData.getBefore());
        assertNotNull(listingData.getModhash());
        assertEquals(listingData.getModhash(), "");
    }
}