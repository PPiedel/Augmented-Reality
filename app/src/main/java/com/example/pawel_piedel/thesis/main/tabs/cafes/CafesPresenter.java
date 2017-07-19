package com.example.pawel_piedel.thesis.main.tabs.cafes;

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

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.CLIENT_ID;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.GRANT_TYPE;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.builder;
import static com.example.pawel_piedel.thesis.api.ServiceFactory.client;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public class CafesPresenter implements CafesContract.Presenter {
    private final static String LOG_TAG = CafesPresenter.class.getName();
    private final CafesContract.View cafesView;
    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    List<Business> cafes = new ArrayList<>();


    public CafesPresenter(@NonNull CafesContract.View cafesView, SharedPreferences sharedPreferences) {
        this.cafesView = checkNotNull(cafesView,"cafesView cannot be null");
        this.sharedPreferences = sharedPreferences;
        cafesView.setPresenter(this);
    }

    @Override
    public void start() {
        getAccessToken();

    }

    public void getAccessToken() {
        apiService = ServiceFactory.createService(ApiService.class);
        apiService.getAccessToken(CLIENT_ID,CLIENT_SECRET,GRANT_TYPE)
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
                        ServiceFactory.accessToken = accessToken;
                        Log.v(LOG_TAG,"Cafes presenter" +accessToken.toString());
                    }
                });
    }

    @Override
    public void loadCafes() {
        apiService = ServiceFactory.createService(ApiService.class);
        apiService.getBusinessesList(50.03,22.01)
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
                        cafes.addAll(searchResponse.getBusinesses());
                        Log.v(LOG_TAG,searchResponse.toString());
                    }
                });

    }

}
