package com.nguyenmp.reader.data;


import com.nguyenmp.reddit.data.LoginData;

public class Account {
    public String username;
    public LoginData data;

    public Account(String username, LoginData data) {
        this.username = username;
        this.data = data;
    }

    public Account(Account other) {
        this.username = other.username;
        this.data = other.data == null ? null : other.data.clone();
    }

    @Override
    public Account clone() {
        return new Account(this);
    }

    @Override
    public String toString() {
        return username;
    }
}