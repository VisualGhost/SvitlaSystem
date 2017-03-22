package com.svitlasystem.ui.map;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.svitlasystem.R;
import com.svitlasystem.content_provider.ProviderContract;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends MapFragment
        implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = LocationFragment.class.getSimpleName();

    private List<Spot> mSpotList;
    private GoogleMap mMap;
    private LatLngBounds.Builder mBuilder;

    public LocationFragment() {
        mSpotList = new ArrayList<>();
        mBuilder = new LatLngBounds.Builder();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        mMap = googleMap;
        if (mSpotList.size() > 0) {
            for (Spot spot : mSpotList) {
                addMarker(mMap, spot.latitude, spot.longitude, spot.name);
            }
            showAllMarkers();
        }
    }

    private void addMarker(GoogleMap map, double lat, double lon, String title) {
        Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(title));
        mBuilder.include(marker.getPosition());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ProviderContract.LOCATION_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int rowsCount = cursor.getCount();
        Log.d(TAG, "onLoadFinished, rows: " + rowsCount);
        if (rowsCount > 0) {
            mSpotList.clear();
            while (cursor.moveToNext()) {
                Spot spot = new Spot();
                spot.name = cursor.getString(1);
                spot.latitude = cursor.getDouble(2);
                spot.longitude = cursor.getDouble(3);
                mSpotList.add(spot);
            }
            if (mMap != null) {
                for (Spot spot : mSpotList) {
                    addMarker(mMap, spot.latitude, spot.longitude, spot.name);
                }
                showAllMarkers();
                mSpotList.clear();
            }
        }
    }

    private void showAllMarkers() {
        if (getView() != null) {
            getView().post(new Runnable() {
                @Override
                public void run() {
                    CameraUpdate cameraUpdate = CameraUpdateFactory
                            .newLatLngBounds(mBuilder.build(), getContext().getResources()
                                    .getDimensionPixelSize(R.dimen.camera_update_padding));
                    mMap.moveCamera(cameraUpdate);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
        mSpotList.clear();
    }

    private static class Spot {
        String name;
        double latitude;
        double longitude;
    }
}


