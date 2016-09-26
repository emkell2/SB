package com.smashingboxes.code_challenge_android.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.smashingboxes.code_challenge_android.database.DatabaseManager;
import com.smashingboxes.code_challenge_android.database.tables.ItemsTable;
import com.smashingboxes.code_challenge_android.preferences.StatePrefs;
import com.smashingboxes.code_challenge_android.utilities.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Background IntentService that parses and imports the CSV data into the database
 * @author Erin Kelley
 */
public class ImportFileDataService extends IntentService {
    public static final String ITEMS_CSV = "items.csv";
    public static final String TAG = ImportFileDataService.class.getSimpleName();
    public static final String DELIMITER = ",";

    public static final int NUM_COLS = 5;

    private boolean success = false;

    public ImportFileDataService() {
        super(TAG);
    }

    public ImportFileDataService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, ImportFileDataService.class.getSimpleName() + " has started.");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Parse and store the data from items.csv into the database
        importFromCsv();

        // Persist that the data was stored in the database
        StatePrefs.getInstance(getBaseContext()).putBooleanValue(
                Utils.KEY_DATA_IMPORTED, success);

        // Broadcast that the data has been imported into the database
        if (success) {
            Intent broadcastIntent = new Intent(Utils.INTENT_DATA_STORED);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, ImportFileDataService.class.getSimpleName() + " has ended.");
    }

    public void importFromCsv() {
        AssetManager assetManager = getAssets();
        InputStream inStream = null;

        try {
            inStream = assetManager.open(ITEMS_CSV);
        } catch (IOException e) {
            Log.e(TAG, "manager=" + assetManager.toString() + "\n" + Log.getStackTraceString(e));
        }

        if (inStream != null) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line = "";
            DatabaseManager dbManager = DatabaseManager.getInstance();
            SQLiteDatabase db = dbManager.openDatabase();

            db.beginTransaction();
            try {
                // Skip first row of column headers
                buffer.readLine();

                while ((line = buffer.readLine()) != null) {
                    String[] data = line.split(DELIMITER);
                    //Log.d(TAG, "data=" + Arrays.toString(data));

                    if (data.length != NUM_COLS) {
                        Log.d(TAG, "Skipping bad CSV row.");
                        continue;
                    }

                    ContentValues cv = getData(data);
                    //Log.d(TAG, "ContentValues=" + cv.toString());

                    db.insert(ItemsTable.TABLE, null, cv);
                }

                db.setTransactionSuccessful();
                success = true;
            } catch (IOException e) {
                Log.e(TAG, "line=" + line + "\n" + Log.getStackTraceString(e));
            } finally {
                db.endTransaction();
            }
        }
    }

    private static ContentValues getData(String[] data) {
        ContentValues cv = new ContentValues();

        cv.put(ItemsTable.COL_ITEM_ID, data[0].trim());
        cv.put(ItemsTable.COL_UPC, data[1].trim());
        cv.put(ItemsTable.COL_ITEM_DESCRIPTION, data[2].trim());
        cv.put(ItemsTable.COL_MANUFACTURER, data[3].trim());
        cv.put(ItemsTable.COL_BRAND, data[4].trim());

        return cv;
    }
}
