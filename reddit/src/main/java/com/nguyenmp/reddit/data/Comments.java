package com.nguyenmp.reddit.data;

import com.nguyenmp.reddit.nio.Get;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Reader;
import java.io.Serializable;

@JsonDeserialize(using = CommentsDeserializer.class)
@JsonSerialize
public class Comments extends Thing implements Serializable {
    private Listing<Link> parent;
    private Listing<Reply> threads;

    public Listing<Link> getParent() {
        return parent;
    }

    public void setParent(Listing<Link> parent) {
        this.parent = parent;
    }

    public Listing<Reply> getThreads() {
        return threads;
    }

    public void setThreads(Listing<Reply> threads) {
        this.threads = threads;
    }

    public static Comments get(String linkID) throws Exception {
        return new Getter(linkID).runBlockingMode();
    }

    private static class Getter extends Get<Comments> {
        private final String linkID;

        private Getter(String linkID) {
            this.linkID = linkID;
        }

        @Override
        public String getEndpoint() {
            return String.format("comments/%s/_/.json", linkID);
        }

        @Override
        public Comments parseResult(Reader reader) throws Exception {
            return new ObjectMapper().readValue(reader, Comments.class);
        }

        @Override
        public void onComplete(Comments result) {
            System.out.println("result : " + result);
        }

        @Override
        public void onError(Exception e) {

        }
    }
}
