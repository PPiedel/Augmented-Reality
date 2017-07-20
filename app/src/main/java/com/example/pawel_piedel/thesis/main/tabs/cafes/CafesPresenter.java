package com.example.pawel_piedel.thesis.main.tabs.cafes;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.pawel_piedel.thesis.api.ApiService;
import com.example.pawel_piedel.thesis.api.LocationService;
import com.example.pawel_piedel.thesis.api.ServiceFactory;
import com.example.pawel_piedel.thesis.model.AccessToken;
import com.example.pawel_piedel.thesis.model.SearchResponse;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.api.ServiceFactory.CLIENT_ID;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.GRANT_TYPE;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.accessToken;
import static com.example.pawel_piedel.thesis.util.Util.gson;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public class CafesPresenter implements CafesContract.Presenter {
    private final static String LOG_TAG = CafesPresenter.class.getName();
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
    public void onViewPreapred() {
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
            ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(cafesView.getContext());
            if (ActivityCompat.checkSelfPermission(cafesView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(cafesView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
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
        apiService.getBusinessesList(LocationService.mLastLocation.getLatitude(),LocationService.mLastLocation.getLongitude(),20000)
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
