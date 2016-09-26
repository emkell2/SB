package com.smashingboxes.code_challenge_android.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences to persist global app information
 * @author Erin kelley
 */
public class StatePrefs {
    private SharedPreferences mPrefs;
    private static StatePrefs sSelf;

    private static final String PREFS_FILE = "com.smashingboxes.state_prefs";
    public static final String KEY_DATA_IMPORTED = "Data_Imported";

    public StatePrefs(Context context) {
        mPrefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    public static StatePrefs getInstance(Context context) {
        if (sSelf == null) {
            sSelf = new StatePrefs(context);
        }

        return sSelf;
    }

    public void putBooleanValue(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).commit();
    }

    public boolean getBooleanValue(String key) {
        return mPrefs.getBoolean(key, false);
    }
}
