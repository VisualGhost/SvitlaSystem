package com.svitlasystem.ui.map;


import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Utils {

    private Utils() {
        // hide
    }

    public static boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability checker = GoogleApiAvailability.getInstance();

        int status = checker.isGooglePlayServicesAvailable(context);

        if (status == ConnectionResult.SUCCESS) {
            if (Utils.getVersionFromPackageManager(context) >= 2) {
                return (true);
            }
        }
        return false;
    }

    private static int getVersionFromPackageManager(Context context) {
        PackageManager packageManager = context.getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
                        return getMajorVersion(featureInfo.reqGlEsVersion);
                    } else {
                        return 1;
                    }
                }
            }
        }
        return 1;
    }

    private static int getMajorVersion(int glEsVersion) {
        return ((glEsVersion & 0xffff0000) >> 16);
    }
}
