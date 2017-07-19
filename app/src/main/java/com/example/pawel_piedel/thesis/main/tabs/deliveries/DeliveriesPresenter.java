package com.example.pawel_piedel.thesis.main.tabs.deliveries;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.pawel_piedel.thesis.api.ApiService;
import com.example.pawel_piedel.thesis.api.ServiceFactory;
import com.example.pawel_piedel.thesis.model.AccessToken;
import com.example.pawel_piedel.thesis.model.Business;
import com.example.pawel_piedel.thesis.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.api.ServiceFactory.CLIENT_ID;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.GRANT_TYPE;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.accessToken;
import static com.example.pawel_piedel.thesis.util.Util.gson;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */

public class DeliveriesPresenter implements DeliveriesContract.Presenter {
    private final static String LOG_TAG = DeliveriesPresenter.class.getName();
    private DeliveriesContract.View deliveriesView;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;
    private List<Business> deliveries;

    public DeliveriesPresenter(@NonNull DeliveriesContract.View deliveriesView, SharedPreferences sharedPreferences) {
        this.deliveries = new ArrayList<>();
        this.deliveriesView = checkNotNull(deliveriesView);
        this.sharedPreferences = sharedPreferences;
        this.deliveriesView.setPresenter(this);
    }


    @Override
    public void start() {
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

                            loadDeliveries();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(AccessToken accessToken) {
                            ServiceFactory.accessToken = accessToken;
                            Log.v(LOG_TAG, "DeliveriesPresenter " + accessToken.toString());
                        }
                    });
        } else {
            loadDeliveries();
        }
    }

    @Override
    public void loadDeliveries() {
        apiService = ServiceFactory.createService(ApiService.class);
        apiService.getBusinessesList(50.03, 22.01)
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
                        deliveries.addAll(searchResponse.getBusinesses());
                        Log.v(LOG_TAG, searchResponse.toString());
                    }
                });
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
}
