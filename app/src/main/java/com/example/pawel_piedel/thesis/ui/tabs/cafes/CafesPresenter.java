package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.location.Location;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.example.pawel_piedel.thesis.data.ApiService;
import com.example.pawel_piedel.thesis.data.LocationService;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */
@ConfigPersistent
public class CafesPresenter <V extends CafesContract.View> extends BasePresenter<V> implements CafesContract.Presenter<V> {
    private final String LOG_TAG = CafesPresenter.class.getName();
    private ApiService apiService;

    @Inject
    DataManager dataManager;

    @Inject
    public CafesPresenter() {
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

    public void load() {
        dataManager.getAccessToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccessToken>() {
                    @Override
                    public void onCompleted() {
                        manageToLoadCafes();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AccessToken accessToken) {
                        dataManager.saveAccessToken(accessToken);
                    }
                });
    }


    @Override
    public void manageToLoadCafes() {
      dataManager.getLastKnownLocation()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Subscriber<Location>() {
                  @Override
                  public void onCompleted() {
                      loadCafes();
                  }

                  @Override
                  public void onError(Throwable e) {

                  }

                  @Override
                  public void onNext(Location location) {
                        LocationService.mLastLocation = location;
                  }
              });

    }

    private void loadCafes() {
        dataManager.loadBusinesses("coffee")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SearchResponse searchResponse) {
                        getView().showCafes(searchResponse.getBusinesses());
                    }
                });
    }

}
