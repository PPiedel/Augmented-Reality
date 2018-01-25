package com.example.pawel_piedel.thesis.data.business;

import android.location.Location;
import android.support.annotation.NonNull;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Pawel_Piedel on 04.11.2017.
 */

public interface BusinessRepository {

    Observable<SearchResponse> loadBusinesses(String term, String category, Location location);

    Observable<Business> loadBusinessDetails(String id);

    Observable<ReviewsResponse> loadReviews(String id);

    List<Business> getAugumentedRealityPlaces();

    boolean getFromSharedPreferences(String label, boolean value);

    String getFromSharedPreferences(String label);

    void saveInSharedPreferences(String stringToSave, String label);

    void saveInSharedPreferences(String label, boolean value);

    void safelyUnsubscribe(Subscription subscription);

    void removeFromSharedPreferences(String label);

    void addClosestPlacesToAugumentedRealityPlaces();

    void saveBusinesses(@NonNull List<Business> businesses, String category);

    boolean isFirstTimeLunched();

}
