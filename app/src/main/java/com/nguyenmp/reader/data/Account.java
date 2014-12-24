package com.nguyenmp.reader.data;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.nguyenmp.reddit.CookieSession;

public class Account {
    @DatabaseField(id = true, dataType = DataType.STRING)
    public String username;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public CookieSession data;

    public Account() {
        // For ORM Lite
    }

    public Account(String username, CookieSession data) {
        this.username = username;
        this.data = data;
    }

    public Account(Account other) {
        this.username = other.username;
        this.data = other.data;
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