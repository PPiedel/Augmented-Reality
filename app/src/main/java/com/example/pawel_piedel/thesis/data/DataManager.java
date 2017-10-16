package com.example.pawel_piedel.thesis.data;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Pair;

import com.example.pawel_piedel.thesis.data.local.SharedPreferencesManager;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.example.pawel_piedel.thesis.data.remote.ApiService;
import com.example.pawel_piedel.thesis.data.remote.ServiceFactory;
import com.example.pawel_piedel.thesis.injection.ApplicationContext;
import com.example.pawel_piedel.thesis.ui.tabs.cafes.CafesPresenter;
import com.example.pawel_piedel.thesis.ui.tabs.deliveries.DeliveriesPresenter;
import com.example.pawel_piedel.thesis.ui.tabs.restaurants.RestaurantsPresenter;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.pawel_piedel.thesis.data.remote.ServiceFactory.CLIENT_ID;
import static com.example.pawel_piedel.thesis.data.remote.ServiceFactory.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.data.remote.ServiceFactory.GRANT_TYPE;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */
@Singleton
public class DataManager {
    public static final String LOCALE = "pl_PL";
    public static final int INITIAL_CAPACITY = 60;
    private final String LOG_TAG = DataManager.class.getSimpleName();
    private ReactiveLocationProvider locationProvider;
    private SharedPreferencesManager preferencesHelper;
    private ApiService apiService;
    private List<Business> restaurants = new ArrayList<>(20);
    private List<Business> cafes = new ArrayList<>(20);
    private List<Business> deliveries = new ArrayList<>(20);
    private Location lastLocation;

    private List<Business> augumentedRealityPlaces;


    @Inject
    DataManager(@ApplicationContext Context context,
                ApiService apiService,
                SharedPreferencesManager preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
        this.apiService = apiService;
        locationProvider = new ReactiveLocationProvider(context);
        this.augumentedRealityPlaces = new ArrayList<>(20);
    }

    public List<Business> getAugumentedRealityPlaces() {
        return augumentedRealityPlaces;
    }

    public void addClosestPlacesToAugumentedRealityPlaces() {
        List<Business> closestPlaces = new ArrayList<>(INITIAL_CAPACITY);
        closestPlaces.addAll(restaurants);
        closestPlaces.addAll(cafes);
        closestPlaces.addAll(deliveries);
        Log.d(LOG_TAG, "Size : " + closestPlaces.size());


        Collections.sort(closestPlaces, Business::compareTo);


        augumentedRealityPlaces.addAll(closestPlaces.subList(0, 20));
        for (Business business : augumentedRealityPlaces){
            Log.d(LOG_TAG,""+business.toString());
        }
        Log.d(LOG_TAG,"Augumented reality size : "+augumentedRealityPlaces.size());
    }

    public Observable<AccessToken> loadAccessToken() {
        AccessToken accessToken = preferencesHelper.getAccessToken();

        if (accessToken == null) {
            return apiService.getAccessToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET);
        } else {
            return Observable.just(accessToken);
        }
    }


    @RequiresPermission(ACCESS_FINE_LOCATION)
    public Observable<Location> getLastKnownLocation() {
        if (lastLocation != null) {
            Log.d(LOG_TAG, lastLocation.toString());
            return Observable.just(lastLocation);
        } else {
            return locationProvider.getLastKnownLocation();
        }
    }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    public Observable<Location> getLocationUpdates() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5 * 1000)
                .setFastestInterval(1000);
        return locationProvider.getUpdatedLocation(request);
    }

    @SuppressLint("MissingPermission")
    public Observable<Pair<AccessToken, Location>> loadAccessTokenLocationPair() {
        return Observable
                .zip(
                        loadAccessToken(),
                        getLastKnownLocation(),
                        Pair::create);
    }


    public void safelyUnsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public Observable<SearchResponse> loadBusinesses(String term, String category) {
        Observable<SearchResponse> observable;
        if (Objects.equals(category, CafesPresenter.CAFES) && cafes != null && !cafes.isEmpty()) {
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setBusinesses(cafes);
            observable = Observable.just(searchResponse);
        } else if (Objects.equals(category, RestaurantsPresenter.RESTAURANTS) && restaurants != null && !restaurants.isEmpty()) {
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setBusinesses(restaurants);
            observable = Observable.just(searchResponse);
        } else if (Objects.equals(category, DeliveriesPresenter.DELIVERIES) && deliveries != null && !deliveries.isEmpty()) {
            SearchResponse searchResponse = new SearchResponse();
            searchResponse.setBusinesses(deliveries);
            observable = Observable.just(searchResponse);
        } else { //non cached
            apiService = ServiceFactory.createService(ApiService.class);
            observable = apiService.getBusinessesList(
                    term,
                    lastLocation.getLatitude(),
                    lastLocation.getLongitude()
            );
        }
        return observable;
    }

    public Observable<Business> loadBusinessDetails(String id) {

        return apiService.getBusinessDetails(id);

    }

    public Observable<ReviewsResponse> loadReviews(String id) {
        Observable<ReviewsResponse> observable;
        //apiService = ServiceFactory.createService(ApiService.class);
        observable = apiService.getBusinessReviews(id, LOCALE);
        return observable;
    }

    public void saveAccessToken(AccessToken accessToken) {
        Log.d(LOG_TAG, "Saving access token : " + accessToken.getAccessToken());
        ServiceFactory.accessToken = accessToken;
        preferencesHelper.saveAccessToken(accessToken);
    }

    public void setLastLocation(Location location) {
        Log.d(LOG_TAG, location.toString());
        this.lastLocation = location;
    }

    public synchronized void saveBusinesses(@NonNull List<Business> businesses, String category) {
        checkNotNull(businesses);
        switch (category) {
            case CafesPresenter.CAFES:
                cafes.addAll(businesses);
                Collections.sort(cafes, Business::compareTo);

                break;
            case RestaurantsPresenter.RESTAURANTS:
                restaurants.addAll(businesses);
                Collections.sort(restaurants, Business::compareTo);
                break;
            case DeliveriesPresenter.DELIVERIES:
                deliveries.addAll(businesses);
                Collections.sort(deliveries, Business::compareTo);
                break;
            default:
                Log.d(LOG_TAG, "Kategoria nierozpoznana");
                break;

        }
    }

    public List<Business> getRestaurants() {
        return restaurants;
    }

    public List<Business> getCafes() {
        return cafes;
    }

    public List<Business> getDeliveries() {
        return deliveries;
    }

    public void setRestaurants(List<Business> restaurants) {
        this.restaurants = restaurants;
    }

    public void setCafes(List<Business> cafes) {
        this.cafes = cafes;
    }

    public void setDeliveries(List<Business> deliveries) {
        this.deliveries = deliveries;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

}
