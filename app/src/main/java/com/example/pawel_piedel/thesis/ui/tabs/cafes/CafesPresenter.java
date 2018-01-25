package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.Manifest;
import android.location.Location;

import com.example.pawel_piedel.thesis.data.auth.AccessTokenRepository;
import com.example.pawel_piedel.thesis.data.business.BusinessRepository;
import com.example.pawel_piedel.thesis.data.location.LocationRepository;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by Pawel_Piedel on 18.07.2017.
 */
@ConfigPersistent
public class CafesPresenter<V extends CafesContract.View> extends BasePresenter<V> implements CafesContract.Presenter<V> {
    public final static String CAFES = "cafes";
    private final static String LOG_TAG = CafesPresenter.class.getName();
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    CafesPresenter(BusinessRepository businessRepository, LocationRepository locationRepository, AccessTokenRepository accessTokenRepository) {
        super(businessRepository, locationRepository, accessTokenRepository);
    }

    public Observable<Boolean> requestPermissions() {
        return view.getRxPermissions()
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE);
    }

    @Override
    public void managePermissions() {
        requestPermissions()
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        loadCafes();
                    }
                });
    }

    @Override
    public void clearSubscriptions() {
        subscriptions.clear();
    }

    public void loadCafes() {
        view.showProgressDialog();
        subscriptions.add(accessTokenRepository.loadAccessToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccessToken>() {
                    @Override
                    public void onCompleted() {
                        loadLocation();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProgressDialog();
                    }

                    @Override
                    public void onNext(AccessToken accessToken) {
                        accessTokenRepository.saveAccessToken(accessToken);


                    }
                })
        );
    }

    public void loadLocation() {
        subscriptions.add(locationRepository.getLastKnownLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Location>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProgressDialog();
                    }

                    @Override
                    public void onNext(Location location) {
                        // view.hideProgressDialog();
                        loadFromApi(location);
                    }
                }));
    }

    private void loadFromApi(Location location) {
        subscriptions.add(businessRepository.loadBusinesses("cafe", CAFES, location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResponse>() {
                    @Override
                    public void onCompleted() {
                        view.hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProgressDialog();
                    }

                    @Override
                    public void onNext(SearchResponse searchResponse) {
                        view.hideProgressDialog();
                        view.showCafes(searchResponse.getBusinesses());
                        businessRepository.saveBusinesses(searchResponse.getBusinesses(), CAFES);
                    }
                }));
    }


}
