package com.nguyenmp.reader.db;

import android.provider.BaseColumns;

public final class AccountContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public AccountContract() {}

    /* Inner class that defines the table contents */
    public static abstract class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_USERNAME= "username";
        public static final String COLUMN_NAME_COOKIE = "cookie";
        public static final String COLUMN_NAME_MODHASH = "modhash";
    }
}