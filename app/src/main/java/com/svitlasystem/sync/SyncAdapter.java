package com.svitlasystem.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.svitlasystem.BuildConfig;
import com.svitlasystem.model.CloudData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d("Test", "PERFORM SYNC");
//        try {
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder().url(BuildConfig.BASE_URL).build();
//            Response response = client.newCall(request).execute();
//
//            if (response.isSuccessful()) {
//                Reader in = response.body().charStream();
//                BufferedReader reader = new BufferedReader(in);
//
//                CloudData cloudData = new Gson().fromJson(reader, CloudData.class);
//                Log.d("Test", "CloudData: " + cloudData.locations.get(0).getLatitude());
//                reader.close();
//            }
//        } catch (IOException e) {
//            Log.e(TAG, e.toString());
//        }
    }
}
