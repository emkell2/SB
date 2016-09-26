package com.smashingboxes.code_challenge_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.smashingboxes.code_challenge_android.database.tables.ItemsTable;
import com.smashingboxes.code_challenge_android.utilities.Utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Helper class for the DatabaseManager to create tables and provide database access
 * @author Erin Kelley
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SB_Challenge_Database";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableIfNotExists(db, ItemsTable.TABLE, ItemsTable.COLUMN_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(String.format("%s %s", Utils.DROP_TABLE_IF_EXISTS,
                ItemsTable.TABLE));
    }

    public boolean createTableIfNotExists(SQLiteDatabase db, String tableName, ColumnInfo[] colsInfoArr) {
        if (colsInfoArr == null || colsInfoArr.length == 0) {
            return false;
        }

        ArrayList<ColumnInfo> tableColumns = new ArrayList<>();
        Collections.addAll(tableColumns, colsInfoArr);

        // Start SQL statement
        String sql = String.format("%s %s (", Utils.CREATE_TABLE_IF_NOT_EXISTS, tableName);

        int colCount = tableColumns.size();
        for (int i = 0; i < colCount; i++) {
            sql += String.format("%s %s", tableColumns.get(i).mColName, tableColumns.get(i).mColType);
            if (i < colCount - 1) {
                sql += ", ";  // middle columns
            } else {
                sql += ");";  // last column
            }
        }
        Log.d("SQL", "sql= " + sql);

        db.execSQL(sql);

        return true;
    }
}
