package com.nguyenmp.reader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nguyenmp.reader.data.Account;

import java.sql.SQLException;
import java.util.List;

public class AccountsDatabase extends OrmLiteSqliteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Accounts.db";

    public AccountsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Account.class);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Account.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public static Account[] get(@NonNull Context context) {
        try {
            OrmHelper helper = OpenHelperManager.getHelper(context, OrmHelper.class);
            Dao<Account, String> dao = helper.getDao();
            List<Account> accounts = dao.queryForAll();
            return accounts.toArray(new Account[accounts.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return new Account[] {};
    }

    public static void put(Context context, Account account) {
        try {
            OrmHelper helper = OpenHelperManager.getHelper(context, OrmHelper.class);
            Dao<Account, String> dao = helper.getDao();
            dao.createOrUpdate(account);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
}
