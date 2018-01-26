package com.example.pawel_piedel.thesis.data.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.example.pawel_piedel.thesis.injection.ApplicationContext;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import javax.inject.Inject;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by PPiedel on 20.01.2018.
 */

public class LocationRepositoryImpl implements LocationRepository {
    private static final int EXPIRATION_DURATION = 10000;
    private static final int INTERVAL = 5000;
    private static final float SMALLEST_DISPLACEMENT = 2;

    private ReactiveLocationProvider reactiveLocationProvider;
    private Location lastLocation;
    private LocationRequest lastKnownLocationRequest;

    @Inject
    public LocationRepositoryImpl(@ApplicationContext Context context) {
        this.reactiveLocationProvider = new ReactiveLocationProvider(context);
    }

    @SuppressLint("MissingPermission")
    @Override
    public Observable<Location> getLastKnownLocation() {
        return reactiveLocationProvider.getUpdatedLocation(lastKnownLocationRequest)
                .filter(location -> !location.equals(lastLocation));
    }


    @Override
    public void cacheLocation(Location location) {
        lastLocation = location;
    }

    @Override
    public Observable<LocationSettingsResult> getLocationSettingsResult() {
        lastKnownLocationRequest = buildLastKnownLocationRequest();
        return reactiveLocationProvider
                .checkLocationSettings(
                        new LocationSettingsRequest.Builder()
                                .setAlwaysShow(true)
                                .addLocationRequest(lastKnownLocationRequest)//Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                .build()
                );
    }

    @NonNull
    private LocationRequest buildLastKnownLocationRequest() {
        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setExpirationDuration(EXPIRATION_DURATION); //according to https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest.html#setNumUpdates(int)

    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(ACCESS_FINE_LOCATION)
    public Observable<Location> getLocationUpdates() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(INTERVAL)
                .setFastestInterval(1000)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT);


        return reactiveLocationProvider
                .getUpdatedLocation(request)
                .subscribeOn(Schedulers.io());
    }
}
