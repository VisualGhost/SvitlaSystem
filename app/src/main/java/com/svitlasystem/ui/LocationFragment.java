package com.svitlasystem.ui;


import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

public class LocationFragment extends MapFragment implements OnMapReadyCallback {

    private boolean needsInit;

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        if (bundle == null) {
            needsInit = true;
        }

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
