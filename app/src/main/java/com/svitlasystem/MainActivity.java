package com.svitlasystem;

import android.accounts.Account;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.svitlasystem.content_provider.ProviderContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(0, null, this);

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        Account account = new Account("SyncAccount", "stubAuthenticator");
        ContentResolver.setIsSyncable(account, "com.svitlasystem.CloudDataProvider", 1);
        ContentResolver.setSyncAutomatically(account, "com.svitlasystem.CloudDataProvider", true);
        ContentResolver.addPeriodicSync(account, "com.svitlasystem.CloudDataProvider", Bundle.EMPTY, 60L);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, ProviderContract.BEER_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d("Test", String.valueOf(cursor != null ? cursor.getCount() : "-1"));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
