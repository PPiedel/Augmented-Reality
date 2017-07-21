package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.pawel_piedel.thesis.BaseView;
import com.example.pawel_piedel.thesis.data.ApiService;
import com.example.pawel_piedel.thesis.data.LocationService;
import com.example.pawel_piedel.thesis.data.ServiceFactory;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.data.ServiceFactory.CLIENT_ID;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.GRANT_TYPE;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.accessToken;
import static com.example.pawel_piedel.thesis.util.Util.gson;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public class CafesPresenter implements CafesContract.Presenter {
    private final String LOG_TAG = CafesPresenter.class.getName();
    private CafesContract.View cafesView;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;


    public CafesPresenter(@NonNull CafesContract.View cafesView, SharedPreferences sharedPreferences) {
        this.cafesView = checkNotNull(cafesView);
        this.sharedPreferences = sharedPreferences;
        this.cafesView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void attachView(BaseView view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void onViewPrepared() {
        load();
    }

    public void load() {
        ServiceFactory.accessToken = retrieveAccessTokenFromSharedPref();
        if (accessToken == null) {
            apiService = ServiceFactory.createService(ApiService.class);
            apiService.getAccessToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AccessToken>() {
                        @Override
                        public void onCompleted() {
                            saveAccessTokenInSharedPref();

                            manageToLoadCafes();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(AccessToken accessToken) {
                            ServiceFactory.accessToken = accessToken;
                            Log.v(LOG_TAG, "Cafes presenter" + accessToken.toString());
                        }
                    });
        } else {
            manageToLoadCafes();
        }

    }

    public void saveAccessTokenInSharedPref() {
        sharedPreferences
                .edit()
                .putString("access_token", gson.toJson(accessToken))
                .apply();
    }

    public AccessToken retrieveAccessTokenFromSharedPref() {
        String json = sharedPreferences
                .getString("access_token", "");
        Log.v(LOG_TAG, "Retrieved access token : " + json);
        return gson.fromJson(json, AccessToken.class);
    }

    @Override
    public void manageToLoadCafes() {
        if (LocationService.mLastLocation != null) {
            loadCafes();
        } else {
            ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(cafesView.provideContext());
            if (ActivityCompat.checkSelfPermission(cafesView.provideContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(cafesView.provideContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationProvider.getLastKnownLocation()
                    .subscribe(new Action1<Location>() {
                        @Override
                        public void call(Location location) {
                            LocationService.mLastLocation = location;
                            Log.i(LOG_TAG,"Location obtained : "+ location.toString());
                            loadCafes();
                        }
                    });
        }


    }

    private void loadCafes() {
        apiService = ServiceFactory.createService(ApiService.class);
        apiService.getBusinessesList(
                "coffee",
                LocationService.mLastLocation.getLatitude(),
                LocationService.mLastLocation.getLongitude(),
                null,
                null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v(LOG_TAG, e.toString());
                    }

                    @Override
                    public void onNext(SearchResponse searchResponse) {
                        cafesView.showCafes(searchResponse.getBusinesses());
                    }
                });
    }

}
