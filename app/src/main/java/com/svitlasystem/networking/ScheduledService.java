package com.svitlasystem.networking;


import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.svitlasystem.BuildConfig;
import com.svitlasystem.content_provider.ProviderContract;
import com.svitlasystem.database.DBContract;
import com.svitlasystem.model.Beer;
import com.svitlasystem.model.CloudData;
import com.svitlasystem.model.Location;
import com.svitlasystem.schedule.PollReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScheduledService extends IntentService {

    private static final String TAG = ScheduledService.class.getSimpleName();

    public ScheduledService() {
        super(ScheduledService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        try {
            CloudData cloudData = loadData();
            if (cloudData != null) {
                applyBatch(cloudData);
            }
        } catch (IOException | RemoteException | OperationApplicationException e) {
            Log.e(TAG, e.toString());
        }
        PollReceiver.completeWakefulIntent(intent);
    }

    private CloudData loadData() throws IOException {
        HttpURLConnection httpURLConnection = null;
        CloudData cloudData = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(BuildConfig.BASE_URL).openConnection();
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            cloudData = new Gson().fromJson(reader, CloudData.class);

            reader.close();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return cloudData;
    }

    private void applyBatch(CloudData cloudData) throws RemoteException, OperationApplicationException {
        if (cloudData != null) {
            ArrayList<ContentProviderOperation> operations = new ArrayList<>();
            operations.add(ContentProviderOperation
                    .newDelete(ProviderContract.BEER_URI).build());

            operations.add(ContentProviderOperation
                    .newDelete(ProviderContract.LOCATION_URI)
                    .build());

            List<Location> locationList = cloudData.locations;
            if (locationList != null && locationList.size() > 0) {
                for (Location location : locationList) {
                    ContentProviderOperation contentProviderOperation =
                            ContentProviderOperation.newInsert(ProviderContract.LOCATION_URI)

                                    .withValue(DBContract.Location.ID, location.getId())
                                    .withValue(DBContract.Location.NAME, location.getName())
                                    .withValue(DBContract.Location.LATITUDE, location.getLatitude())
                                    .withValue(DBContract.Location.LONGITUDE, location.getLongitude())
                                    .build();

                    operations.add(contentProviderOperation);
                }
            }

            List<Beer> beerList = cloudData.beers;
            if (beerList != null && beerList.size() > 0) {
                for (Beer beer : beerList) {
                    ContentProviderOperation contentProviderOperation =
                            ContentProviderOperation.newInsert(ProviderContract.BEER_URI)

                                    .withValue(DBContract.Beer.ID, beer.getId())
                                    .withValue(DBContract.Beer.NAME, beer.getName())
                                    .build();

                    operations.add(contentProviderOperation);
                }
            }

            getApplicationContext().getContentResolver().applyBatch(ProviderContract.CONTENT_AUTHORITY, operations);

        }
    }
}
