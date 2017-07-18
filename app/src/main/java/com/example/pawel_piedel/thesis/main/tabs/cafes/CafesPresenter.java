package com.example.pawel_piedel.thesis.main.tabs.cafes;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.pawel_piedel.thesis.api.ApiService;
import com.example.pawel_piedel.thesis.api.NetworkService;
import com.example.pawel_piedel.thesis.main.GetBusinessesCallback;
import com.example.pawel_piedel.thesis.model.AccessToken;
import com.example.pawel_piedel.thesis.model.Business;
import com.example.pawel_piedel.thesis.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.schedulers.NewThreadScheduler;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.api.NetworkService.CLIENT_ID;
import static com.example.pawel_piedel.thesis.api.NetworkService.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.api.NetworkService.GRANT_TYPE;
import static com.example.pawel_piedel.thesis.api.NetworkService.builder;
import static com.example.pawel_piedel.thesis.api.NetworkService.client;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public class CafesPresenter implements CafesContract.Presenter {
    private final static String LOG_TAG = CafesPresenter.class.getName();
    private final CafesContract.View cafesView;
    private ApiService apiService;
    List<Business> businesses = new ArrayList<>();
    boolean isAccessTokenLoaded = false;


    public CafesPresenter(@NonNull CafesContract.View cafesView) {
        this.cafesView = checkNotNull(cafesView,"cafesView cannot be null");
        cafesView.setPresenter(this);
    }

    @Override
    public void start() {
        Retrofit retrofit = builder
                .client(client)
                .build();
        ApiService authClient = retrofit.create(ApiService.class);
        authClient.getAccessToken(CLIENT_ID,CLIENT_SECRET,GRANT_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccessToken>() {
                    @Override
                    public void onCompleted() {
                        Log.v(LOG_TAG,"on completed");
                        loadCafes();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AccessToken accessToken) {
                        NetworkService.accessToken = accessToken;
                        Log.v(LOG_TAG,"Cafes presenter" +accessToken.toString());
                    }
                });

    }

    @Override
    public void loadCafes() {
        ApiService client = NetworkService.createService(ApiService.class);
        client.getBusinessesList(50.03,22.01)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v(LOG_TAG,e.toString());
                    }

                    @Override
                    public void onNext(SearchResponse searchResponse) {
                        cafesView.showCafes(searchResponse.getBusinesses());
                        Log.v(LOG_TAG,searchResponse.toString());
                    }
                });

    }

}
