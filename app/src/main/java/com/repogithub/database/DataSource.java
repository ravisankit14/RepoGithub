package com.repogithub.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.repogithub.model.GetRepo;
import com.repogithub.model.Owner;

import java.util.ArrayList;
import java.util.List;


public class DataSource {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    SQLiteOpenHelper mDbHelper;

    public DataSource(Context context) {
        this.mContext = context;
        mDbHelper = new DBHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public GetRepo createItem(GetRepo item) {
        ContentValues values = item.toValues();
        mDatabase.insert(ItemsTable.TABLE_ITEMS, null, values);
        return item;
    }

    public long getDataItemsCount() {
        return DatabaseUtils.queryNumEntries(mDatabase, ItemsTable.TABLE_ITEMS);
    }

    public void seedDatabase(List<GetRepo> dataItemList) {
        long numItems = getDataItemsCount();
        if (numItems == 0) {
            for (GetRepo item :
                    dataItemList) {
                try {
                    createItem(item);
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<GetRepo> getAllItems() {
        List<GetRepo> dataItems = new ArrayList<>();

        Cursor cursor = null;

        cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
                null, null, null, null, ItemsTable.COLUMN_NAME);


        while (cursor.moveToNext()) {
            GetRepo item = new GetRepo();
            Owner owner = new Owner();

            item.setName(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_NAME)));
            item.setHtml_url(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_HTTP_URL)));
            item.setDescription(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_DESCRIPTION)));
            item.setSize(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_SIZE)));
            item.setWatchers_count(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_WATCHER)));
            item.setOpen_issues_count(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_ISSUES)));

            owner.setAvatar_url(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_IMAGE)));
            item.setOwner(owner);
        }
        cursor.close();
        return dataItems;
    }
}
