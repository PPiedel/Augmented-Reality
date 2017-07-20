package com.example.pawel_piedel.thesis.data;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

public class LocationService {
    public static Location mLastLocation;
    private static final String LOG_TAG = LocationService.class.getSimpleName();

    private FusedLocationProviderClient mFusedLocationClient;

    private Context mContext;

   public LocationService(Context context){
       mContext = context;
       mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
   }


}
