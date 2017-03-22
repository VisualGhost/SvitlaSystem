package com.svitlasystem.content_provider;


import android.content.ContentUris;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.svitlasystem.database.DBContract;

class QueryHelper {

    private QueryHelper() {
        // hide
    }

    static Cursor getBeers(SQLiteDatabase db, String[] projection, String selection,
                           String[] selectionArgs, String orderBy) {
        return db.query(DBContract.Table.BEER, projection, selection, selectionArgs, null, null, orderBy);
    }

    static Cursor getOneBeer(SQLiteDatabase db, Uri uri, String[] projection, String orderBy) {

        String selection = DBContract.Beer.ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return db.query(DBContract.Table.BEER, projection, selection, selectionArgs, null, null, orderBy);
    }

    static Cursor getLocations(SQLiteDatabase db, String[] projection, String selection,
                               String[] selectionArgs, String orderBy) {
        return db.query(DBContract.Table.LOCATION, projection, selection, selectionArgs, null, null, orderBy);
    }

    static Cursor getOneLocation(SQLiteDatabase db, Uri uri, String[] projection, String orderBy) {

        String selection = DBContract.Location.ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return db.query(DBContract.Table.LOCATION, projection, selection, selectionArgs, null, null, orderBy);
    }
}
