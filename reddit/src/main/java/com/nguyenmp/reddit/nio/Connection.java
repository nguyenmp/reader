package com.nguyenmp.reddit.nio;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public abstract class Connection<ResultType> implements Runnable {
    private static final String BASE_URL = "http://www.reddit.com/";

    @Override
    public void run() {
        try {
            onComplete(runBlockingMode());
        } catch (Exception e) {
            onError(e);
        }
    }

    public ResultType runBlockingMode() throws Exception {
        String endpoint = getEndpoint();
        String target = endpoint == null ? getBaseURL() : getBaseURL().concat(endpoint);

        URL url = new URL(target);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        initializeConnection(urlConnection);

        InputStream inputStream = urlConnection.getInputStream();
        Reader reader = new InputStreamReader(inputStream);
        ResultType result = parseResult(reader);

        urlConnection.disconnect();
        reader.close();

        return result;
    }

    public static String getBaseURL() {
        return BASE_URL;
    }

    public abstract String getEndpoint();

    public abstract void initializeConnection(HttpURLConnection connection) throws Exception;

    public abstract ResultType parseResult(Reader reader) throws Exception;

    public abstract void onComplete(ResultType result);
    public abstract void onError(Exception e);

    public String asString(Reader reader) {
        Scanner scanner = new Scanner(reader);
        scanner.useDelimiter("\\A");
        return scanner.next();
    }
}
