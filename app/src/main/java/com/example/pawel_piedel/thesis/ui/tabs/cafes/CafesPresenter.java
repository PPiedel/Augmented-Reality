package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.Manifest;
import android.location.Location;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.pawel_piedel.thesis.data.BusinessDataSource;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Pawel_Piedel on 18.07.2017.
 */
@ConfigPersistent
public class CafesPresenter<V extends CafesContract.View> extends BasePresenter<V> implements CafesContract.Presenter<V> {
    public final static String CAFES = "cafes";
    private final static String LOG_TAG = CafesPresenter.class.getName();
    private RxPermissions rxPermissions;

    @Inject
    CafesPresenter(BusinessDataSource businessDataSource) {
        super(businessDataSource);
    }

    @Override
    public void onViewPrepared() {
        load();
    }

    public void load() {
        rxPermissions = new RxPermissions(getView().getParentActivity());
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE)
                .subscribe(granted -> {
                    getView().showProgressDialog();
                    getBusinessDataSource().loadAccessTokenLocationPair()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Pair<AccessToken, Location>>() {
                                @Override
                                public void onCompleted() {
                                    loadCafes();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(LOG_TAG, e.getMessage());
                                    getView().hideProgressDialog();
                                    Toast.makeText(getView().getParentActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onNext(Pair<AccessToken, Location> accessTokenLocationPair) {
                                    if (accessTokenLocationPair.second == null){
                                        getView().showAlert("Lokalizacja","Twoja lokalizacja nie mogłą zostać ustalona.");
                                    }
                                    getBusinessDataSource().saveAccessToken(accessTokenLocationPair.first);
                                    getBusinessDataSource().setLastLocation(accessTokenLocationPair.second);
                                }
                            });
                });
    }

    public void loadCafes() {
        if (getBusinessDataSource().getLastLocation() != null) {
            getBusinessDataSource().loadBusinesses("coffee", CAFES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SearchResponse>() {
                        @Override
                        public void onCompleted() {
                            getView().hideProgressDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(LOG_TAG, e.getMessage());
                            getView().hideProgressDialog();
                            Toast.makeText(getView().getParentActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(SearchResponse searchResponse) {
                            if (!searchResponse.getBusinesses().isEmpty()) {
                                getBusinessDataSource().saveBusinesses(searchResponse.getBusinesses(), CAFES);
                            }
                            getView().hideProgressDialog();
                            getView().showCafes(searchResponse.getBusinesses());
                        }
                    });
        }
        else {
            getView().hideProgressDialog();
            getView().showAlert("Lokalizacja","Twoja lokalizacja nie mogłą zostać ustalona.");

        }
    }

    @Override
    public void managePermissions() {
        RxPermissions rxPermissions = new RxPermissions(getView().getParentActivity());
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        onViewPrepared();
                    }
                });
    }
}
