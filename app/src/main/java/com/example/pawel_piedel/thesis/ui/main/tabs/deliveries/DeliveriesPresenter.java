package com.example.pawel_piedel.thesis.ui.main.tabs.deliveries;

import android.location.Location;
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
 * Created by Pawel_Piedel on 19.07.2017.
 */
@ConfigPersistent
public class DeliveriesPresenter<V extends DeliveriesContract.View> extends BasePresenter<V> implements DeliveriesContract.Presenter<V> {
    private final static String LOG_TAG = DeliveriesPresenter.class.getName();
    public final static String DELIVERIES = "deliveries";


    @Inject
    public DeliveriesPresenter(DataManager dataManager) {
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
        loadDeliveries();
    }

    public void loadDeliveries() {
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
                        loadFromApi();
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

    public void loadFromApi() {
        getDataManager().loadBusinesses("delivery", DELIVERIES)
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
                        if (!searchResponse.getBusinesses().isEmpty()){
                            getDataManager().saveBusinesses(searchResponse.getBusinesses(), DELIVERIES);
                        }
                        getView().showDeliveries(searchResponse.getBusinesses());
                    }
                });
    }

}
