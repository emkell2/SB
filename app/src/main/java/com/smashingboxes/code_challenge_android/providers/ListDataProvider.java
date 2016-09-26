package com.smashingboxes.code_challenge_android.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.smashingboxes.code_challenge_android.database.DatabaseManager;
import com.smashingboxes.code_challenge_android.database.tables.ItemsTable;

/**
 * ContentProvider which provides data through a ContentResolver
 * @author Erin Kelley
 */
public class ListDataProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = ItemsTable.SORT_ORDER_DEFAULT;
        }

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(ItemsTable.TABLE);

        SQLiteDatabase db = DatabaseManager.getInstance().getReadableDatabase();

        Cursor cursor = builder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
