package com.nguyenmp.reddit.nio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.LinkedList;

/** https://stackoverflow.com/questions/2793150/how-to-use-java-net-urlconnection-to-fire-and-handle-http-requests */
public abstract class Post<ResultType> extends Connection<ResultType> {
    private static final String charset = "UTF-8";

    @Override
    public void initializeConnection(HttpURLConnection connection) throws IOException {
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

        OutputStream output = connection.getOutputStream();
        output.write(getQuery().getBytes());
    }

    public abstract Query getQuery();

    public static class Query extends LinkedList<NameValuePair> {
        public Query() {
            super();
        }

        public Query(Collection<? extends NameValuePair> c) {
            super(c);
        }

        public byte[] getBytes() {
            return toString().getBytes();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            Query query = this.clone();

            boolean first = true;
            for (NameValuePair pair : query) {
                if (!first) {
                    builder.append("&");
                } else {
                    first = false;
                }

                builder.append(pair.toString());
            }

            return builder.toString();
        }

        @Override
        public Query clone() {
            return new Query(this);
        }
    }

    public static class NameValuePair {
        public final String name, value;

        public NameValuePair(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public NameValuePair(NameValuePair other) {
            this.name = other.name;
            this.value = other.value;
        }

        @Override
        public int hashCode() {
            return name.hashCode() + value.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof NameValuePair) {
                NameValuePair nameValuePair = (NameValuePair) other;
                return this.name.equals(nameValuePair.name) && this.value.equals(nameValuePair.value);
            } else {
                return super.equals(other);
            }
        }

        /**
         * Encodes the name value pair for url posting
         */
        @Override
        public String toString() {
            return String.format("%s=%s", URLEncoder.encode(name), URLEncoder.encode(value));
        }

        @Override
        protected NameValuePair clone() {
            return new NameValuePair(this);
        }
    }
}
