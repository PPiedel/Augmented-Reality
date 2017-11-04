package com.example.pawel_piedel.thesis.ui.tabs.restaurants;

import android.Manifest;
import android.location.Location;
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
 * Created by Pawel_Piedel on 20.07.2017.
 */
@ConfigPersistent
public class RestaurantsPresenter<V extends RestaurantsContract.View> extends BasePresenter<V> implements RestaurantsContract.Presenter<V> {
    public final static String RESTAURANTS = "restaurants";
    private final static String LOG_TAG = RestaurantsPresenter.class.getSimpleName();
    private RxPermissions rxPermissions;

    @Inject
    RestaurantsPresenter(BusinessDataSource businessDataSource) {
        super(businessDataSource);

    }

    @Override
    public void onViewPrepared() {
        loadRestaurannts();
    }

    public void loadRestaurannts() {
        rxPermissions = new RxPermissions(getView().getParentActivity());
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        getView().showProgressDialog();
                        getBusinessDataSource().loadAccessTokenLocationPair()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Pair<AccessToken, Location>>() {
                                    @Override
                                    public void onCompleted() {
                                        loadFromApi();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        getView().hideProgressDialog();
                                        Toast.makeText(getView().getParentActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onNext(Pair<AccessToken, Location> accessTokenLocationPair) {
                                        if (accessTokenLocationPair.second == null) {
                                            getView().showAlert("Lokalizacja", "Twoja lokalizacja nie mogłą zostać ustalona.");
                                        }
                                        getBusinessDataSource().saveAccessToken(accessTokenLocationPair.first);
                                        getBusinessDataSource().setLastLocation(accessTokenLocationPair.second);
                                    }
                                });
                    }
                });
    }

    private void loadFromApi() {
        if (getBusinessDataSource().getLastLocation() != null) {
            getBusinessDataSource().loadBusinesses("restaurant", RESTAURANTS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SearchResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().hideProgressDialog();
                            Toast.makeText(getView().getParentActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(SearchResponse searchResponse) {
                            if (!searchResponse.getBusinesses().isEmpty()) {
                                getBusinessDataSource().saveBusinesses(searchResponse.getBusinesses(), RESTAURANTS);
                            }

                            getView().hideProgressDialog();
                            getView().showRestaurants(searchResponse.getBusinesses());

                        }
                    });
        } else {
            getView().hideProgressDialog();
            getView().showAlert("Lokalizacja", "Twoja lokalizacja nie mogłą zostać ustalona.");

        }

    }

}

