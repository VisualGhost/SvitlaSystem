package com.svitlasystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.svitlasystem.content_provider.CloudDataProvider;
import com.svitlasystem.database.DBContract;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.svitlasystem.content_provider.ProviderContract.BEER_URI;
import static com.svitlasystem.content_provider.ProviderContract.CONTENT_AUTHORITY;
import static com.svitlasystem.content_provider.ProviderContract.LOCATION_URI;

@RunWith(AndroidJUnit4.class)
public class CloudDataProviderTest extends ProviderTestCase2<CloudDataProvider> {

    private static final MockContentResolver RESOLVER = new MockContentResolver();

    private CloudDataProvider mCloudDataProvider;
    private Class<CloudDataProvider> mDataProviderClass;

    public CloudDataProviderTest() {
        super(CloudDataProvider.class, CONTENT_AUTHORITY);
        setName(CloudDataProvider.class.getSimpleName());
        mDataProviderClass = CloudDataProvider.class;
    }

    @Override
    public CloudDataProvider getProvider() {
        return mCloudDataProvider;
    }

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        mCloudDataProvider = mDataProviderClass.newInstance();
        Assert.assertNotNull(mCloudDataProvider);
        mCloudDataProvider.attachInfo(context, null);
        RESOLVER.addProvider(CONTENT_AUTHORITY, mCloudDataProvider);

    }

    @After
    public void shutDown() throws Exception {
        getProvider().delete(BEER_URI, null, null);
        getProvider().delete(LOCATION_URI, null, null);
        getProvider().shutdown();
    }

    @Test
    public void getBeersIsCorrect() throws Exception {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Beer.ID, 1);
        contentValues.put(DBContract.Beer.NAME, "Tripel Karmeliet");
        contentValues.put(DBContract.Beer.TYPE, "Tripel");
        getProvider().insert(BEER_URI, contentValues);

        contentValues = new ContentValues();
        contentValues.put(DBContract.Beer.ID, 2);
        contentValues.put(DBContract.Beer.NAME, "Sierra Nevada Kellerweis");
        contentValues.put(DBContract.Beer.TYPE, "Hefeweizen");
        getProvider().insert(BEER_URI, contentValues);


        Cursor cursor = getProvider().query(BEER_URI, null, null, null, null);
        try {
            int actual = cursor.getCount();
            Assert.assertEquals(2, actual);

            cursor.moveToFirst();
            Assert.assertEquals(1, cursor.getInt(0));
            Assert.assertEquals("Tripel Karmeliet", cursor.getString(1));
            Assert.assertEquals("Tripel", cursor.getString(2));

            cursor.moveToNext();
            Assert.assertEquals(2, cursor.getInt(0));
            Assert.assertEquals("Sierra Nevada Kellerweis", cursor.getString(1));
            Assert.assertEquals("Hefeweizen", cursor.getString(2));
        } finally {
            cursor.close();
        }
    }

    @Test
    public void getOneBeerIsCorrect() throws Exception {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Beer.ID, 1);
        contentValues.put(DBContract.Beer.NAME, "Tripel Karmeliet");
        contentValues.put(DBContract.Beer.TYPE, "Tripel");
        getProvider().insert(BEER_URI, contentValues);

        contentValues = new ContentValues();
        contentValues.put(DBContract.Beer.ID, 2);
        contentValues.put(DBContract.Beer.NAME, "Sierra Nevada Kellerweis");
        contentValues.put(DBContract.Beer.TYPE, "Hefeweizen");
        getProvider().insert(BEER_URI, contentValues);


        Cursor cursor = getProvider().query(BEER_URI, null, DBContract.Beer.ID + "=?", new String[]{"1"}, null);
        try {
            int actual = cursor.getCount();
            Assert.assertEquals(1, actual);

            cursor.moveToFirst();
            Assert.assertEquals(1, cursor.getInt(0));
            Assert.assertEquals("Tripel Karmeliet", cursor.getString(1));
            Assert.assertEquals("Tripel", cursor.getString(2));

        } finally {
            cursor.close();
        }

        cursor = getProvider().query(BEER_URI, null, DBContract.Beer.ID + "=?", new String[]{"2"}, null);
        try {
            int actual = cursor.getCount();
            Assert.assertEquals(1, actual);

            cursor.moveToFirst();
            Assert.assertEquals(2, cursor.getInt(0));
            Assert.assertEquals("Sierra Nevada Kellerweis", cursor.getString(1));
            Assert.assertEquals("Hefeweizen", cursor.getString(2));

        } finally {
            cursor.close();
        }
    }

    @Test
    public void getLocationsIsCorrect() throws Exception {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Location.ID, 1);
        contentValues.put(DBContract.Location.NAME, "Co Nam");
        contentValues.put(DBContract.Location.LATITUDE, 37.792094);
        contentValues.put(DBContract.Location.LONGITUDE, -122.421169);
        getProvider().insert(LOCATION_URI, contentValues);

        contentValues = new ContentValues();
        contentValues.put(DBContract.Location.ID, 2);
        contentValues.put(DBContract.Location.NAME, "Hawaii West");
        contentValues.put(DBContract.Location.LATITUDE, 37.798345);
        contentValues.put(DBContract.Location.LONGITUDE, -122.409307);
        getProvider().insert(LOCATION_URI, contentValues);


        Cursor cursor = getProvider().query(LOCATION_URI, null, null, null, null);
        try {
            int actual = cursor.getCount();
            Assert.assertEquals(2, actual);

            cursor.moveToFirst();
            Assert.assertEquals(1, cursor.getInt(0));
            Assert.assertEquals("Co Nam", cursor.getString(1));
            Assert.assertEquals(37.792094, cursor.getDouble(2), 0.0000001);
            Assert.assertEquals(-122.421169, cursor.getDouble(3), 0.0000001);

            cursor.moveToNext();
            Assert.assertEquals(2, cursor.getInt(0));
            Assert.assertEquals("Hawaii West", cursor.getString(1));
            Assert.assertEquals(37.798345, cursor.getDouble(2), 0.0000001);
            Assert.assertEquals(-122.409307, cursor.getDouble(3), 0.0000001);
        } finally {
            cursor.close();
        }
    }

    @Test
    public void getOneLocationIsCorrect() throws Exception {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.Location.ID, 1);
        contentValues.put(DBContract.Location.NAME, "Co Nam");
        contentValues.put(DBContract.Location.LATITUDE, 37.792094);
        contentValues.put(DBContract.Location.LONGITUDE, -122.421169);
        getProvider().insert(LOCATION_URI, contentValues);

        contentValues = new ContentValues();
        contentValues.put(DBContract.Location.ID, 2);
        contentValues.put(DBContract.Location.NAME, "Hawaii West");
        contentValues.put(DBContract.Location.LATITUDE, 37.798345);
        contentValues.put(DBContract.Location.LONGITUDE, -122.409307);
        getProvider().insert(LOCATION_URI, contentValues);

        Cursor cursor = getProvider().query(LOCATION_URI, null, DBContract.Location.ID + "=?", new String[]{"1"}, null);
        try {

            int actual = cursor.getCount();
            Assert.assertEquals(1, actual);
            cursor.moveToFirst();

            Assert.assertEquals(1, cursor.getInt(0));
            Assert.assertEquals("Co Nam", cursor.getString(1));
            Assert.assertEquals(37.792094, cursor.getDouble(2), 0.0000001);
            Assert.assertEquals(-122.421169, cursor.getDouble(3), 0.0000001);

        } finally {
            cursor.close();
        }

        cursor = getProvider().query(LOCATION_URI, null, DBContract.Location.ID + "=?", new String[]{"2"}, null);
        try {

            int actual = cursor.getCount();
            Assert.assertEquals(1, actual);
            cursor.moveToFirst();

            Assert.assertEquals(2, cursor.getInt(0));
            Assert.assertEquals("Hawaii West", cursor.getString(1));
            Assert.assertEquals(37.798345, cursor.getDouble(2), 0.0000001);
            Assert.assertEquals(-122.409307, cursor.getDouble(3), 0.0000001);

        } finally {
            cursor.close();
        }
    }

}

