package com.example.pawel_piedel.thesis.api;

import android.content.Context;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

public class LocationService {
    private static final String LOG_TAG = LocationService.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;

   LocationService(Context context){
       mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

   }

}
