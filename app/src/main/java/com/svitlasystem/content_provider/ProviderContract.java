package com.svitlasystem.content_provider;


import android.net.Uri;

import com.svitlasystem.BuildConfig;
import com.svitlasystem.database.DBContract;

public class ProviderContract {

    private ProviderContract() {
        // hide
    }

    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID + "." + CloudDataProvider.class.getSimpleName();
    public static final String PATH_BEER = DBContract.Table.BEER;
    public static final String PATH_LOCATION = DBContract.Table.LOCATION;
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final Uri BEER_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BEER);
    public static final Uri LOCATION_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LOCATION);

}
