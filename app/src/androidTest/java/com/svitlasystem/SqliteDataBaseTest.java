package com.svitlasystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.svitlasystem.content_provider.CloudDataProvider;
import com.svitlasystem.database.DBContract;
import com.svitlasystem.database.DBUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;

@RunWith(AndroidJUnit4.class)
public class SQLiteDatabaseTest {

    private static final String SCHEME_1_FILE_NAME = "clouddata.db.1.sql";
    private static final String TEST_DATABASE_NAME = "test.db";

    private SQLiteDatabase mDatabase;

    @Before
    public void prepareDatabase() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        mDatabase = appContext.openOrCreateDatabase(
                TEST_DATABASE_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null
        );
        Assert.assertNotNull(mDatabase);
    }

    @After
    public void releaseDatabase() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        boolean isDeleted = appContext.deleteDatabase(TEST_DATABASE_NAME);
        Assert.assertTrue(TEST_DATABASE_NAME + " is deleted", isDeleted);
    }

    @Test
    public void sqlSchemeNotNull() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        checkScheme(appContext, SCHEME_1_FILE_NAME);
    }

    private void checkScheme(Context context, String fileSchemeName) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileSchemeName);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Assert.assertFalse(true);
                }
            } else {
                Assert.assertFalse(fileSchemeName + " was not found", true);
            }
        }
    }

    @Test
    public void testCreationTablesVersion_1() throws Exception {
        mDatabase.setVersion(1);
        Assert.assertEquals(1, mDatabase.getVersion());
        Context appContext = InstrumentationRegistry.getTargetContext();
        DBUtils.getSQLStatement(mDatabase, appContext, "clouddata.db", 1);

        Cursor cursor = mDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        try {
            cursor.moveToFirst();
            Assert.assertEquals("android_metadata", cursor.getString(0));
            cursor.moveToNext();
            Assert.assertEquals("beer", cursor.getString(0));
            cursor.moveToNext();
            Assert.assertEquals("location", cursor.getString(0));
        } finally {
            cursor.close();
        }
    }

    @Test
    public void testReadWriteBeerTableVersion_1() throws Exception {
        mDatabase.setVersion(1);
        Context appContext = InstrumentationRegistry.getTargetContext();
        DBUtils.getSQLStatement(mDatabase, appContext, "clouddata.db", 1);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Beer.ID, 1);
        contentValues.put(DBContract.Beer.NAME, "Tripel Karmeliet");
        contentValues.put(DBContract.Beer.TYPE, "Tripel");
        mDatabase.insert(DBContract.Table.BEER, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(DBContract.Beer.ID, 2);
        contentValues.put(DBContract.Beer.NAME, "Sierra Nevada Kellerweis");
        contentValues.put(DBContract.Beer.TYPE, "Hefeweizen");
        mDatabase.insert(DBContract.Table.BEER, null, contentValues);

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + DBContract.Table.BEER, null);
        try {
            cursor.moveToFirst();
            Assert.assertEquals(cursor.getInt(0), 1);
            Assert.assertEquals(cursor.getString(1), "Tripel Karmeliet");
            Assert.assertEquals(cursor.getString(2), "Tripel");

            cursor.moveToNext();
            Assert.assertEquals(cursor.getInt(0), 2);
            Assert.assertEquals(cursor.getString(1), "Sierra Nevada Kellerweis");
            Assert.assertEquals(cursor.getString(2), "Hefeweizen");
        } finally {
            cursor.close();
        }
    }

    @Test
    public void testReadWriteLocationTableVersion_1() throws Exception {
        mDatabase.setVersion(1);
        Context appContext = InstrumentationRegistry.getTargetContext();
        DBUtils.getSQLStatement(mDatabase, appContext, "clouddata.db", 1);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Location.ID, 1);
        contentValues.put(DBContract.Location.NAME, "Co Nam");
        contentValues.put(DBContract.Location.LATITUDE, 37.792094);
        contentValues.put(DBContract.Location.LONGITUDE, -122.421169);
        mDatabase.insert(DBContract.Table.LOCATION, null, contentValues);

        contentValues = new ContentValues();
        contentValues.put(DBContract.Location.ID, 2);
        contentValues.put(DBContract.Location.NAME, "Hawaii West");
        contentValues.put(DBContract.Location.LATITUDE, 37.798345);
        contentValues.put(DBContract.Location.LONGITUDE, -122.409307);
        mDatabase.insert(DBContract.Table.LOCATION, null, contentValues);

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + DBContract.Table.LOCATION, null);
        try {
            cursor.moveToFirst();
            Assert.assertEquals(cursor.getInt(0), 1);
            Assert.assertEquals(cursor.getString(1), "Co Nam");
            Assert.assertEquals(cursor.getDouble(2), 37.792094, 0.0000001);
            Assert.assertEquals(cursor.getDouble(3), -122.421169, 0.0000001);

            cursor.moveToNext();
            Assert.assertEquals(cursor.getInt(0), 2);
            Assert.assertEquals(cursor.getString(1), "Hawaii West");
            Assert.assertEquals(cursor.getDouble(2), 37.798345, 0.0000001);
            Assert.assertEquals(cursor.getDouble(3), -122.409307, 0.0000001);
        } finally {
            cursor.close();
        }
    }
}
