package com.nguyenmp.reader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nguyenmp.reader.data.Account;
import com.nguyenmp.reddit.data.LoginData;

import java.util.ArrayList;
import java.util.List;

import static com.nguyenmp.reader.db.AccountContract.AccountEntry;

public class AccountsDatabase extends SQLiteOpenHelper{
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AccountEntry.TABLE_NAME + " (" +
                    AccountEntry._ID + " INTEGER PRIMARY KEY," +
                    AccountEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    AccountEntry.COLUMN_NAME_COOKIE + TEXT_TYPE + COMMA_SEP +
                    AccountEntry.COLUMN_NAME_MODHASH + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Accounts.db";

    public AccountsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static Account[] get(Context context) {
        // These are the query arguments
        String table = AccountEntry.TABLE_NAME;
        String[] columns = null; // All columns
        String selection = null; // No filter
        String[] selectionArgs = null; // Because selection is null
        String groupBy = null; // No grouping
        String having = null; // No grouping
        String orderBy = AccountEntry.COLUMN_NAME_USERNAME;

        // Query the database
        SQLiteDatabase database = new AccountsDatabase(context).getReadableDatabase();
        Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        // Parse database info into a result
        List<Account> accounts = new ArrayList<Account>();
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_NAME_USERNAME));
            String modhash = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_NAME_MODHASH));
            String cookie = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_NAME_COOKIE));

            LoginData login = new LoginData(cookie, modhash);
            Account account = new Account(username, login);
            accounts.add(account);
        }

        // Clean resources
        cursor.close();
        database.close();

        // Return the generated list of accounts from the DB
        return accounts.toArray(new Account[accounts.size()]);
    }

    public static void put(Context context, Account account) {
        // Get DB
        SQLiteDatabase database = new AccountsDatabase(context).getWritableDatabase();

        // Convert POJO values into DB values
        ContentValues values = new ContentValues();
        values.put(AccountEntry.COLUMN_NAME_USERNAME, account.username);
        values.put(AccountEntry.COLUMN_NAME_MODHASH, account.data.modhash);
        values.put(AccountEntry.COLUMN_NAME_COOKIE, account.data.cookie);

        // Push data into DB
        database.insert(AccountEntry.TABLE_NAME, null, values);

        // Clean up
        database.close();
    }
}
