package com.svitlasystem.content_provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.svitlasystem.database.DBContract;
import com.svitlasystem.database.DBHelper;

import java.util.ArrayList;

import static com.svitlasystem.content_provider.ProviderContract.CONTENT_AUTHORITY;
import static com.svitlasystem.content_provider.ProviderContract.PATH_BEER;
import static com.svitlasystem.content_provider.ProviderContract.PATH_LOCATION;


public class CloudDataProvider extends ContentProvider {

    private static final int BEERS = 1;
    private static final int BEER_ID = 2;
    private static final int LOCATIONS = 3;
    private static final int LOCATION_ID = 4;

    private static final UriMatcher MATCHER;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI(CONTENT_AUTHORITY, PATH_BEER, BEERS);
        MATCHER.addURI(CONTENT_AUTHORITY, PATH_BEER + "/#", BEER_ID);
        MATCHER.addURI(CONTENT_AUTHORITY, PATH_LOCATION, LOCATIONS);
        MATCHER.addURI(CONTENT_AUTHORITY, PATH_LOCATION + "/#", LOCATION_ID);
    }

    private DBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            mDBHelper = new DBHelper(getContext());
        }
        return mDBHelper != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String orderBy) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = MATCHER.match(uri);
        switch (match) {
            case BEERS:
                cursor = QueryHelper.getBeers(db, projection, selection, selectionArgs, orderBy);
                break;
            case BEER_ID:
                cursor = QueryHelper.getOneBeer(db, uri, projection, orderBy);
                break;
            case LOCATIONS:
                cursor = QueryHelper.getLocations(db, projection, selection, selectionArgs, orderBy);
                break;
            case LOCATION_ID:
                cursor = QueryHelper.getOneLocation(db, uri, projection, orderBy);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        int matcher = MATCHER.match(uri);
        switch (matcher) {
            case BEERS:
                return String.format("%s/vnd.%s.%s",
                        ContentResolver.CURSOR_DIR_BASE_TYPE,
                        CONTENT_AUTHORITY,
                        PATH_BEER
                );
            case LOCATIONS:
                return String.format("%s/vnd.%s.%s",
                        ContentResolver.CURSOR_DIR_BASE_TYPE,
                        CONTENT_AUTHORITY,
                        PATH_LOCATION
                );
            case BEER_ID:
                return String.format("%s/vnd.%s.%s",
                        ContentResolver.CURSOR_ITEM_BASE_TYPE,
                        CONTENT_AUTHORITY,
                        PATH_BEER
                );
            case LOCATION_ID:
                return String.format("%s/vnd.%s.%s",
                        ContentResolver.CURSOR_ITEM_BASE_TYPE,
                        CONTENT_AUTHORITY,
                        PATH_LOCATION
                );
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues cv) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long id;
        int matcher = MATCHER.match(uri);
        switch (matcher) {
            case BEERS:
                id = db.insertOrThrow(DBContract.Table.BEER, null, cv);
                break;
            case LOCATIONS:
                id = db.insertOrThrow(DBContract.Table.LOCATION, null, cv);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int match = MATCHER.match(uri);
        int rowCount;
        switch (match) {
            case BEERS:
                rowCount = db.delete(DBContract.Table.BEER, selection, selectionArgs);
                break;
            case BEER_ID:
                selection = DBContract.Beer.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowCount = db.delete(DBContract.Table.BEER, selection, selectionArgs);
                break;
            case LOCATIONS:
                rowCount = db.delete(DBContract.Table.LOCATION, selection, selectionArgs);
                break;
            case LOCATION_ID:
                selection = DBContract.Location.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowCount = db.delete(DBContract.Table.LOCATION, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowCount;
    }

    @Override
    public int update(Uri uri, ContentValues cv, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int matcher = MATCHER.match(uri);
        int rowCount;

        switch (matcher) {
            case BEERS:
                rowCount = db.update(DBContract.Table.BEER, cv, selection, selectionArgs);
                break;
            case BEER_ID:
                selection = DBContract.Beer.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowCount = db.update(DBContract.Table.BEER, cv, selection, selectionArgs);
                break;
            case LOCATIONS:
                rowCount = db.update(DBContract.Table.LOCATION, cv, selection, selectionArgs);
                break;
            case LOCATION_ID:
                selection = DBContract.Location.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowCount = db.update(DBContract.Table.LOCATION, cv, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);

        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return rowCount;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentProviderResult[] results = super.applyBatch(operations);
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void shutdown() {
        mDBHelper.close();
        super.shutdown();
    }
}
