package com.example.pawel_piedel.thesis.data.business;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.business.local.LocalDataSource;
import com.example.pawel_piedel.thesis.data.business.remote.BusinessRemoteDataSource;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.example.pawel_piedel.thesis.ui.tabs.cafes.CafesPresenter;
import com.example.pawel_piedel.thesis.ui.tabs.deliveries.DeliveriesPresenter;
import com.example.pawel_piedel.thesis.ui.tabs.restaurants.RestaurantsPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */
public class BusinessRepositoryImpl implements BusinessRepository {
    public static final String FIRST_TIME_LUNCHED = "first_time_lunched";

    private static final String LOCALE = "pl_PL";
    private static final int MAX_CAPACITY = 60;
    private static final int AUGUMENTED_LIST_MAX_CAPACITY = 20;
    private static final String LOG_TAG = BusinessRepositoryImpl.class.getSimpleName();

    private LocalDataSource localDataSource;
    private BusinessRemoteDataSource remoteSource;

    private List<Business> restaurants;
    private List<Business> cafes;
    private List<Business> deliveries;
    private List<Business> augumentedRealityPlaces;


    @Inject
    public BusinessRepositoryImpl(BusinessRemoteDataSource remoteSource,
                                  LocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
        this.remoteSource = remoteSource;
    }

    @Override
    public List<Business> getAugumentedRealityPlaces() {
        return augumentedRealityPlaces;
    }

    public void addClosestPlacesToAugumentedRealityPlaces() {
        List<Business> closestPlaces = new ArrayList<>(MAX_CAPACITY);
        augumentedRealityPlaces = new ArrayList<>(AUGUMENTED_LIST_MAX_CAPACITY);
        if (restaurants != null) {
            closestPlaces.addAll(restaurants);
        }
        if (cafes != null) {
            closestPlaces.addAll(cafes);
        }
        if (deliveries != null) {
            closestPlaces.addAll(deliveries);
        }

        Collections.sort(closestPlaces, Business::compareTo);


        if (!closestPlaces.isEmpty()) {
            augumentedRealityPlaces.addAll(closestPlaces.subList(0, 20));
        }
        Log.d(LOG_TAG, "Augumented reality size : " + augumentedRealityPlaces.size());
    }

    public void safelyUnsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public Observable<SearchResponse> loadBusinesses(String term, String category, Location location) {
        if (Objects.equals(category, CafesPresenter.CAFES) && cafes != null && !cafes.isEmpty()) {
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setBusinesses(cafes);
            return Observable.just(searchResponse);
        } else if (Objects.equals(category, RestaurantsPresenter.RESTAURANTS) && restaurants != null && !restaurants.isEmpty()) {
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setBusinesses(restaurants);
            return Observable.just(searchResponse);
        } else if (Objects.equals(category, DeliveriesPresenter.DELIVERIES) && deliveries != null && !deliveries.isEmpty()) {
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setBusinesses(deliveries);
            return Observable.just(searchResponse);
        } else {
            return remoteSource.loadBusinesses(
                    term,
                    location.getLatitude(),
                    location.getLongitude()
            );

        }
    }

    @Override
    public Observable<Business> loadBusinessDetails(String id) {
        return remoteSource.loadBusinessDetails(id);
    }

    @Override
    public Observable<ReviewsResponse> loadReviews(String id) {
        Observable<ReviewsResponse> observable;
        observable = remoteSource.getBusinessReviews(id, LOCALE);
        return observable;
    }

    @Override
    public synchronized void saveBusinesses(@NonNull List<Business> businesses, String category) {
        checkNotNull(businesses);
        switch (category) {
            case CafesPresenter.CAFES:
                cafes = new ArrayList<>(businesses.size());
                cafes.addAll(businesses);
                Collections.sort(cafes, Business::compareTo);
                break;
            case RestaurantsPresenter.RESTAURANTS:
                restaurants = new ArrayList<>(businesses.size());
                restaurants.addAll(businesses);
                Collections.sort(restaurants, Business::compareTo);
                break;
            case DeliveriesPresenter.DELIVERIES:
                deliveries = new ArrayList<>(businesses.size());
                deliveries.addAll(businesses);
                Collections.sort(deliveries, Business::compareTo);
                break;
            default:
                Log.d(LOG_TAG, "Kategoria nierozpoznana");
                break;

        }
    }

    public void saveInSharedPreferences(String label, boolean value) {
        localDataSource.putBoolean(label, value);
    }

    @Override
    public boolean getFromSharedPreferences(String label, boolean value) {
        return localDataSource.getBoolean(label, value);
    }

    @Override
    public void saveInSharedPreferences(String stringToSave, String label) {
        localDataSource.saveString(stringToSave, label);
    }

    @Override
    public String getFromSharedPreferences(String label) {
        return localDataSource.getString(label);
    }

    public void removeFromSharedPreferences(String label) {
        localDataSource.removeFromSharedPreferences(label);
    }

    public boolean isFirstTimeLunched() {
        return getFromSharedPreferences(FIRST_TIME_LUNCHED, true);
    }

    public List<Business> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Business> restaurants) {
        this.restaurants = restaurants;
    }

    public List<Business> getCafes() {
        return cafes;
    }

    public void setCafes(List<Business> cafes) {
        this.cafes = cafes;
    }

    public List<Business> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Business> deliveries) {
        this.deliveries = deliveries;
    }

}
