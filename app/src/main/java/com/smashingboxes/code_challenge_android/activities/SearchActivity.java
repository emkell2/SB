package com.smashingboxes.code_challenge_android.activities;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smashingboxes.code_challenge_android.Item;
import com.smashingboxes.code_challenge_android.R;
import com.smashingboxes.code_challenge_android.database.DatabaseManager;
import com.smashingboxes.code_challenge_android.database.tables.ItemsTable;
import com.smashingboxes.code_challenge_android.preferences.StatePrefs;
import com.smashingboxes.code_challenge_android.services.ImportFileDataService;
import com.smashingboxes.code_challenge_android.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity used to search data that was imported from a CSV file
 * @author Erin Kelley
 */
public class SearchActivity extends AppCompatActivity {
    private TextView mNumResults;
    private ItemListAdapter mItemListAdapter;
    private ArrayList<Item> mItemList = new ArrayList<>();

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (StatePrefs.getInstance(context).getBooleanValue(Utils.KEY_APP_IS_ACTIVE)) {
                // Update RecyclerView display
                initializeRecyclerView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize database
        DatabaseManager.initialize(this);

        // Check if the database has been filled with data
        if (!StatePrefs.getInstance(this).getBooleanValue(Utils.KEY_DATA_IMPORTED)) {
            // Start service
            Intent serviceIntent = new Intent(this, ImportFileDataService.class);
            startService(serviceIntent);
        }

        // Setup Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup number of results TextView
        mNumResults = (TextView) findViewById(R.id.tv_num_results);

        // Setup RecyclerView and items list
        mItemListAdapter = new ItemListAdapter(mItemList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_item_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mItemListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        // Setup SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mItemListAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Save state
        StatePrefs.getInstance(this).putBooleanValue(Utils.KEY_APP_IS_ACTIVE, true);

        if (!StatePrefs.getInstance(this).getBooleanValue(Utils.KEY_DATA_DISPLAYED)) {
            initializeRecyclerView();
        }

        // Register receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Utils.INTENT_DATA_STORED));
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        // Save state
        StatePrefs.getInstance(this).putBooleanValue(Utils.KEY_APP_IS_ACTIVE, false);
    }

    private void initializeRecyclerView() {
        Uri uri = Uri.parse(Utils.CONTENT_URI);
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(
                uri,
                new String[] { ItemsTable.COL_ITEM_DESCRIPTION },
                null,
                null,
                null);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(ItemsTable.COL_ITEM_DESCRIPTION);
                    Item item = new Item(cursor.getString(index));
                    mItemList.add(item);
                }
                mItemListAdapter.notifyDataSetChanged();
                updateNumResults(mItemList.size());

                StatePrefs.getInstance(getBaseContext())
                        .putBooleanValue(Utils.KEY_DATA_DISPLAYED, true);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    private void updateNumResults(int numResults) {
        mNumResults.setText(String.valueOf(numResults));
    }

    public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
        private List<Item> mItemList;

        public ItemListAdapter(List<Item> itemList) {
            mItemList = itemList;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_row, parent, false);

            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Item item = mItemList.get(position);

            holder.itemDescription.setText(item.getItemDescription());
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }

        public void filter(String text) {
            ArrayList<Item> itemListCopy = new ArrayList<>(mItemList);
            mItemList.clear();
            if (TextUtils.isEmpty(text)) {
                // For the sake of time, but this is bad.
                initializeRecyclerView();
            } else {
                text = text.toLowerCase();
                for (Item item: itemListCopy) {
                    if (item.getItemDescription().toLowerCase().contains(text)) {
                        mItemList.add(item);
                    }
                }
            }

            notifyDataSetChanged();
            updateNumResults(mItemList.size());
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView itemDescription;

            public ItemViewHolder(View itemView) {
                super(itemView);
                itemDescription = (TextView) itemView.findViewById(R.id.item_description);
            }
        }
    }
}