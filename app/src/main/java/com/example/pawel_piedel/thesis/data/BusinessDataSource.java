package com.example.pawel_piedel.thesis.data;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.google.android.gms.location.LocationSettingsResult;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Pawel_Piedel on 04.11.2017.
 */

public interface BusinessDataSource {
    Observable<AccessToken> loadAccessToken();

    Observable<Location> getLastKnownLocation();

    Observable<LocationSettingsResult> getLocationSettingsResult();

    Observable<Location> getLocationUpdates();

    Observable<Pair<AccessToken, Location>> loadAccessTokenLocationPair();

    Observable<SearchResponse> loadBusinesses(String term, String category);

    Observable<Business> loadBusinessDetails(String id);

    Observable<ReviewsResponse> loadReviews(String id);

    List<Business> getAugumentedRealityPlaces();

    boolean getFromSharedPreferences(String label, boolean value);

    String getFromSharedPreferences(String label);

    void saveInSharedPreferences(String stringToSave, String label);

    void saveInSharedPreferences(String label, boolean value);

    Location getLastLocation();

    void setLastLocation(Location location);

    void safelyUnsubscribe(Subscription subscription);

    void removeFromSharedPreferences(String label);

    void addClosestPlacesToAugumentedRealityPlaces();

    void saveBusinesses(@NonNull List<Business> businesses, String category);

    void saveAccessToken(AccessToken accessToken);

    boolean isFirstTimeLunched();

}
