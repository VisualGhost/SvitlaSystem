package com.svitlasystem.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class SyncService extends Service {

    private static SyncAdapter sSyncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (SyncService.class) {
            sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
