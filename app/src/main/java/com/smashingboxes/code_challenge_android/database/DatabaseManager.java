package com.smashingboxes.code_challenge_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Manager to manage database access
 * @author Erin kelley
 */
public class DatabaseManager {
    private static DatabaseManager sInstance;
    private static DatabaseHelper sDatabaseHelper;

    private SQLiteDatabase mDatabase;

    public synchronized static void initialize(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager();
            sDatabaseHelper = new DatabaseHelper(context);
        }
    }

    public synchronized static DatabaseManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName()
                    + " is not initialized. Call initialize(...) first.");
        }

        return sInstance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = sDatabaseHelper.getWritableDatabase();
        }

        return mDatabase;
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        return sDatabaseHelper.getReadableDatabase();
    }

    public synchronized void closeDatabase() {
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
        }
    }
}
