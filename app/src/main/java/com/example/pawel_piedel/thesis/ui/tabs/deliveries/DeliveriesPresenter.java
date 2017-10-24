package com.example.pawel_piedel.thesis.ui.tabs.deliveries;

import android.location.Location;
import android.util.Pair;
import android.widget.Toast;

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
    DeliveriesPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public void onViewPrepared() {
        loadDeliveries();
    }

    public void loadDeliveries() {
        getView().showProgressDialog();
        Observable
                .zip(
                        getDataManager().loadAccessToken(),
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
                        getView().hideProgressDialog();
                        Toast.makeText(getView().provideContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Pair<AccessToken, Location> accessTokenLocationPair) {
                        if (accessTokenLocationPair.second ==null){
                            getView().showAlert("Lokalizacja","Twoja lokalizacja nie mogłą zostać ustalona.");
                        }
                        getDataManager().saveAccessToken(accessTokenLocationPair.first);
                        getDataManager().setLastLocation(accessTokenLocationPair.second);
                    }
                });

    }

    private void loadFromApi() {
        if (getDataManager().getLastLocation()!=null){
            getDataManager().loadBusinesses("delivery", DELIVERIES)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<SearchResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().hideProgressDialog();
                            Toast.makeText(getView().provideContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(SearchResponse searchResponse) {
                            if (!searchResponse.getBusinesses().isEmpty()){
                                getDataManager().saveBusinesses(searchResponse.getBusinesses(), DELIVERIES);
                            }
                            getView().hideProgressDialog();
                            getView().showDeliveries(searchResponse.getBusinesses());
                        }
                    });
        }

    }

}
