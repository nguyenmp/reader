package com.nguyenmp.reddit;

import com.nguyenmp.reddit.data.LoginData;
import com.nguyenmp.reddit.data.LoginResult;
import com.nguyenmp.reddit.nio.LoginRunnable;

import java.util.Arrays;

/** An interface to access reddit.com */
public class Reddit {

    private Reddit() {
        // Do nothing
    }

    public static LoginData login(String username, String password) throws Exception {
        LoginResult result = new LoginRunnable(username, password).runBlockingMode();
        if (result.json.errors.length > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            for (String[] error : result.json.errors) {
                builder.append("[");
                for (String message : error) {
                    builder.append(message); builder.append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append("]");
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("]");
            throw new Exception("Failed to log in: " + builder.toString());
        }
        else return result.json.data;
    }
}
