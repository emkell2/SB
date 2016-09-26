package com.smashingboxes.code_challenge_android.utilities;

import com.smashingboxes.code_challenge_android.database.tables.ItemsTable;

/**
 * Utility class for Strings throughout the application
 * @author Erin Kelley
 */
public class Utils {

    // Database Strings
    public static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS";
    public static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS";

    public static final String CONTENT_URI = "content://com.smashingboxes.code_challenge_android.providers.ListDataProvider/" + ItemsTable.TABLE;

    // Intent Strings
    public static final String INTENT_DATA_STORED = "Intent_Data_Stored";

    // SharedPreferences Strings
    public static final String KEY_DATA_IMPORTED = "Data_Imported";
    public static final String KEY_APP_IS_ACTIVE = "App_Is_Active";
    public static final String KEY_DATA_DISPLAYED = "Data_Displayed";
}
