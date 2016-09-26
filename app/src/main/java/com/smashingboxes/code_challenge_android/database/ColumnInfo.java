package com.smashingboxes.code_challenge_android.database;

/**
 * Holder class for column names and types
 * @author Erin Kelley
 */
public class ColumnInfo {
    public String mColName;
    public String mColType;

    public ColumnInfo(String name, String type) {
        this.mColName = name;
        this.mColType = type;
    }
}
