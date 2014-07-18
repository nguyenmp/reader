package com.nguyenmp.reddit.nio;

import org.junit.Test;

import static com.nguyenmp.reddit.nio.Post.NameValuePair;
import static org.junit.Assert.*;

public class NameValuePairTest {

    @Test
    public void testConstructor() {
        new NameValuePair("asdf", "123");
    }

    @Test
    public void testHashCode() {
        NameValuePair first = new NameValuePair("asdf", "123");
        NameValuePair second = new NameValuePair("asdf", "123");
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void testEquals() {
        NameValuePair first = new NameValuePair("asdf", "123");
        NameValuePair second = new NameValuePair("asdf", "123");
        assertEquals(first, second);
        assertEquals(first, first);
        assertEquals(second, first);
        assertEquals(second, second);
    }

    @Test
    public void testToString() {
        NameValuePair a = new NameValuePair("asdf", "123");
        assertEquals(a.toString(), "asdf=123");
    }

    @Test
    public void testClone() throws Exception {
        NameValuePair first = new NameValuePair("asdf", "123");
        assertEquals(first, first.clone());
    }
}