package com.example.pawel_piedel.thesis.main.tabs.cafes;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.pawel_piedel.thesis.api.ApiService;
import com.example.pawel_piedel.thesis.api.ServiceGenerator;
import com.example.pawel_piedel.thesis.model.Business;
import com.example.pawel_piedel.thesis.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public class CafesPresenter implements CafesContract.Presenter {
    private final static String LOG_TAG = CafesPresenter.class.getName();
    private final CafesContract.View cafesView;
    private ApiService apiService;
    List<Business> businesses = new ArrayList<>();


    public CafesPresenter(@NonNull CafesContract.View cafesView) {
        this.cafesView = checkNotNull(cafesView,"cafesView cannot be null");
        cafesView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCafes();
    }

    @Override
    public void loadCafes() {
        apiService =  ServiceGenerator.createService(ApiService.class);
        apiService.getBusinessesList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG,e.toString());
                    }

                    @Override
                    public void onNext(SearchResponse searchResponse) {
                        businesses.addAll(searchResponse.getBusinesses());
                        Log.v(LOG_TAG,businesses.toString());
                    }
                });


    }

    @Override
    public void loadMoreCafes() {

    }

    @Override
    public void openCafeDetail(@NonNull Business cafe) {

    }
}
