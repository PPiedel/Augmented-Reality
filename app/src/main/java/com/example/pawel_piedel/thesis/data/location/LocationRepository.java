package com.example.pawel_piedel.thesis.data.location;

import android.location.Location;

import com.google.android.gms.location.LocationSettingsResult;

import rx.Observable;

/**
 * Created by PPiedel on 20.01.2018.
 */

public interface LocationRepository {
    Observable<Location> getLastKnownLocation();

    void saveLastLocation(Location location);

    Observable<LocationSettingsResult> getLocationSettingsResult();

    Observable<Location> getLocationUpdates();
}
