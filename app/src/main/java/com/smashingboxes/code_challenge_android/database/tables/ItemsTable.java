package com.smashingboxes.code_challenge_android.database.tables;

import com.smashingboxes.code_challenge_android.database.ColumnInfo;

/**
 * Stores Database schema information
 * @author Erin Kelley
 */
public class ItemsTable {

    // Table Name
    public static final String TABLE = "items_list_table";

    // Columns
    public static final String _ID = "_id";
    public static final String COL_ITEM_ID = "ItemID";
    public static final String COL_UPC = "UPC";
    public static final String COL_ITEM_DESCRIPTION = "ItemDescription";
    public static final String COL_MANUFACTURER = "Manufacturer";
    public static final String COL_BRAND = "Brand";

    public static final String SORT_ORDER_DEFAULT = COL_ITEM_DESCRIPTION + " ASC";

    public static final ColumnInfo[] COLUMN_INFO = {
            new ColumnInfo(_ID, "INTEGER PRIMARY KEY AUTOINCREMENT"),
            new ColumnInfo(COL_ITEM_ID, "TEXT"),
            new ColumnInfo(COL_UPC, "TEXT"),
            new ColumnInfo(COL_ITEM_DESCRIPTION, "TEXT"),
            new ColumnInfo(COL_MANUFACTURER, "TEXT"),
            new ColumnInfo(COL_BRAND, "TEXT")
    };
}
