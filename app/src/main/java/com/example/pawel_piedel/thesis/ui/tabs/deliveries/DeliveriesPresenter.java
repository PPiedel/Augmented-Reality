package com.example.pawel_piedel.thesis.ui.tabs.deliveries;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.example.pawel_piedel.thesis.data.ApiService;
import com.example.pawel_piedel.thesis.data.LocationService;
import com.example.pawel_piedel.thesis.data.ServiceFactory;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
 * Created by Pawel_Piedel on 19.07.2017.
 */
@ConfigPersistent
public class DeliveriesPresenter<V extends DeliveriesContract.View> extends BasePresenter<V> implements DeliveriesContract.Presenter<V> {
    private final static String LOG_TAG = DeliveriesPresenter.class.getName();
    private ApiService apiService;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    public DeliveriesPresenter() {
    }


    @Override
    public void start() {

    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    @Override
    public void onViewPrepared() {
        load();
    }

    @Override
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

                            manageToLoadDeliveries();
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
            manageToLoadDeliveries();
        }
    }

    public AccessToken retrieveAccessTokenFromSharedPref() {
        String json = sharedPreferences
                .getString("access_token", "");
        Log.v(LOG_TAG, "Retrieved access token : " + json);
        return gson.fromJson(json, AccessToken.class);
    }

    public void saveAccessTokenInSharedPref() {
        sharedPreferences
                .edit()
                .putString("access_token", gson.toJson(accessToken))
                .apply();
    }

    @Override
    public void manageToLoadDeliveries() {
        if (LocationService.mLastLocation != null) {
            loadDeliveries();
        } else {
            ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getView().provideContext());
            if (ActivityCompat.checkSelfPermission(getView().provideContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getView().provideContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationProvider.getLastKnownLocation()
                    .subscribe(new Action1<Location>() {
                        @Override
                        public void call(Location location) {
                            LocationService.mLastLocation = location;
                            Log.i(LOG_TAG, "Location obtained : " + location.toString());
                            loadDeliveries();
                        }
                    });
        }
    }

    private void loadDeliveries() {
        apiService = ServiceFactory.createService(ApiService.class);
        apiService.getBusinessesList(
                "delivery",
                LocationService.mLastLocation.getLatitude(),
                LocationService.mLastLocation.getLongitude(),
                40000,
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
                        getView().showDeliveries(searchResponse.getBusinesses());
                    }
                });
    }

}
