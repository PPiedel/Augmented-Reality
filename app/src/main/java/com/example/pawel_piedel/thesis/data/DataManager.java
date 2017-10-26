package com.example.pawel_piedel.thesis.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
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
import com.example.pawel_piedel.thesis.ui.main.MainActivity;
import com.example.pawel_piedel.thesis.ui.tabs.cafes.CafesPresenter;
import com.example.pawel_piedel.thesis.ui.tabs.deliveries.DeliveriesPresenter;
import com.example.pawel_piedel.thesis.ui.tabs.restaurants.RestaurantsPresenter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

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
    public static final String FIRST_TIME_LUNCHED = "first_time_lunched";
    public static final int MAX_CAPACITY = 60;
    public static final int AUGUMENTED_LIST_MAX_CAPACITY = 20;
    public static final float SMALLEST_DISPLACEMENT = 5;
    private final String LOG_TAG = DataManager.class.getSimpleName();
    private ReactiveLocationProvider locationProvider;
    private SharedPreferencesManager preferencesHelper;
    private ApiService apiService;
    private List<Business> restaurants;
    private List<Business> cafes;
    private List<Business> deliveries;
    private Location lastLocation;

    private List<Business> augumentedRealityPlaces;
    private LocationRequest lastLocationRequest;

    @Inject
    DataManager(@ApplicationContext Context context,
                ApiService apiService,
                SharedPreferencesManager sharedPreferencesManager) {
        this.preferencesHelper = sharedPreferencesManager;
        this.apiService = apiService;
        locationProvider = new ReactiveLocationProvider(context);
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public List<Business> getAugumentedRealityPlaces() {
        //addClosestPlacesToAugumentedRealityPlaces();
        return augumentedRealityPlaces;
    }

    public void loadAugumentedRealityPlaces() {
        addClosestPlacesToAugumentedRealityPlaces();
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

    public Observable<AccessToken> loadAccessToken() {
        AccessToken accessToken = preferencesHelper.getAccessToken();

        if (accessToken == null) {
            return apiService.getAccessToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET);
        } else {
            return Observable.just(accessToken);
        }
    }


    @SuppressLint("MissingPermission")
    @RequiresPermission(ACCESS_FINE_LOCATION)
    public Observable<Location> getLastKnownLocation() {
        if (lastLocation != null) {
            return Observable.just(lastLocation);
        } else {
            return locationProvider.getUpdatedLocation(lastLocationRequest);
        }
    }

    @NonNull
    private LocationRequest buildLastLocationRequest() {
        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setExpirationDuration(10000); //according to https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest.html#setNumUpdates(int)

    }

    public Observable<LocationSettingsResult> getLocationSettingsResult() {
        lastLocationRequest = buildLastLocationRequest();
        return locationProvider
                .checkLocationSettings(
                        new LocationSettingsRequest.Builder()
                                .setAlwaysShow(true)
                                .addLocationRequest(lastLocationRequest)//Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                .build()
                );
    }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    public Observable<Location> getLocationUpdates() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5 * 1000)
                .setFastestInterval(1000)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT);


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
        //Log.d(LOG_TAG, "Saving access token : " + accessToken.getAccessToken());
        ServiceFactory.accessToken = accessToken;
        preferencesHelper.saveAccessToken(accessToken);
    }

    public void setLastLocation(Location location) {
        // Log.d(LOG_TAG, location.toString());
        this.lastLocation = location;
    }

    public synchronized void saveBusinesses(@NonNull List<Business> businesses, String category) {
        Log.d(LOG_TAG, "Save business");
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
        preferencesHelper.putBoolean(label, value);
    }

    public boolean getFromSharedPreferences(String label, boolean value) {
        return preferencesHelper.getBoolean(label, value);
    }

    public void saveInSharedPreferences(String stringToSave, String label) {
        preferencesHelper.saveString(stringToSave, label);
    }

    public String getFromSharedPreferences(String label) {
        return preferencesHelper.getString(label);
    }

    public void removeFromSharedPreferences(String label) {
        preferencesHelper.removeFromSharedPreferences(label);
    }

    public boolean isFirstTimeLunched() {
        return getFromSharedPreferences(FIRST_TIME_LUNCHED, true);
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
