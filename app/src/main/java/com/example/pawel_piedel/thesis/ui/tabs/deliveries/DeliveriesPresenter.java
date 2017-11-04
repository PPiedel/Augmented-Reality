package com.example.pawel_piedel.thesis.ui.tabs.deliveries;

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

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */
@ConfigPersistent
public class DeliveriesPresenter<V extends DeliveriesContract.View> extends BasePresenter<V> implements DeliveriesContract.Presenter<V> {
    public final static String DELIVERIES = "deliveries";
    private final static String LOG_TAG = DeliveriesPresenter.class.getName();
    private RxPermissions rxPermissions;

    @Inject
    DeliveriesPresenter(BusinessDataSource businessDataSource) {
        super(businessDataSource);
    }


    @Override
    public void managePermissions() {
        rxPermissions = new RxPermissions(getView().getParentActivity());
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        loadDeliveries();
                    }
                });
    }

    public void loadDeliveries() {
        rxPermissions = new RxPermissions(getView().getParentActivity());
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE)
                .subscribe(granted -> {
                    getView().showProgressDialog();
                    Observable
                            .zip(
                                    getBusinessDataSource().loadAccessToken(),
                                    getBusinessDataSource().getLastKnownLocation(),
                                    Pair::create)
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
                                        getView().showAlert("Lokalizacja", "Twoja lokalizacja nie mogła zostać ustalona.");
                                    }
                                    getBusinessDataSource().saveAccessToken(accessTokenLocationPair.first);
                                    getBusinessDataSource().setLastLocation(accessTokenLocationPair.second);
                                }
                            });
                });
    }

    private void loadFromApi() {
        if (getBusinessDataSource().getLastLocation() != null) {
            getBusinessDataSource().loadBusinesses("delivery", DELIVERIES)
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
                                getBusinessDataSource().saveBusinesses(searchResponse.getBusinesses(), DELIVERIES);
                            }
                            getView().hideProgressDialog();
                            getView().showDeliveries(searchResponse.getBusinesses());
                        }
                    });
        }
        else {
            getView().hideProgressDialog();
            getView().showAlert("Lokalizacja","Twoja lokalizacja nie mogłą zostać ustalona.");

        }

    }

}
