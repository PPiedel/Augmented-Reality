package com.example.pawel_piedel.thesis.api;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

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
