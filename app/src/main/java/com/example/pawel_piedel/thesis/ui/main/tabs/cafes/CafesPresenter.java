package com.example.pawel_piedel.thesis.ui.main.tabs.cafes;

import android.location.Location;
import android.util.Log;
import android.util.Pair;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Pawel_Piedel on 18.07.2017.
 */
@ConfigPersistent
public class CafesPresenter<V extends CafesContract.View> extends BasePresenter<V> implements CafesContract.Presenter<V> {
    private final static String LOG_TAG = CafesPresenter.class.getName();
    public final static String CAFES = "cafes";

    @Inject
    public CafesPresenter(DataManager dataManager) {
        super(dataManager);
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

    public void load() {
        getView().showProgressDialog();
        Observable
                .zip(
                        getDataManager().getAccessToken(),
                        getDataManager().getLastKnownLocation(),
                        Pair::create)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Pair<AccessToken, Location>>() {
                    @Override
                    public void onCompleted() {
                        loadCafes();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Pair<AccessToken, Location> accessTokenLocationPair) {
                        getDataManager().saveAccessToken(accessTokenLocationPair.first);
                        getDataManager().saveLocation(accessTokenLocationPair.second);
                    }
                });

    }

    public void loadCafes() {
        getDataManager().loadBusinesses("coffee", CAFES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(SearchResponse searchResponse) {
                        if (!searchResponse.getBusinesses().isEmpty()) {
                            getDataManager().saveBusinesses(searchResponse.getBusinesses(), CAFES);
                        }
                        getView().hideProgressDialog();
                        getView().showCafes(searchResponse.getBusinesses());
                    }
                });

    }


}
