package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BaseFragment;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.example.pawel_piedel.thesis.data.ApiService;
import com.example.pawel_piedel.thesis.data.LocationService;
import com.example.pawel_piedel.thesis.data.ServiceFactory;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;

import javax.inject.Inject;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.data.ServiceFactory.CLIENT_ID;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.GRANT_TYPE;
import static com.example.pawel_piedel.thesis.data.ServiceFactory.accessToken;
import static com.example.pawel_piedel.thesis.util.Util.gson;
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
        if (LocationService.mLastLocation != null) {
            loadCafes();
        } else {
            ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getView().provideContext());
            if (ActivityCompat.checkSelfPermission(getView().provideContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getView().provideContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationProvider.getLastKnownLocation()
                    .subscribe(new Action1<Location>() {
                        @Override
                        public void call(Location location) {
                            LocationService.mLastLocation = location;
                            Log.i(LOG_TAG,"Location obtained : "+ location.toString());
                            loadCafes();
                        }
                    });
        }


    }

    private void loadCafes() {
        apiService = ServiceFactory.createService(ApiService.class);
        apiService.getBusinessesList(
                "coffee",
                LocationService.mLastLocation.getLatitude(),
                LocationService.mLastLocation.getLongitude(),
                null,
                null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v(LOG_TAG, e.toString());
                    }

                    @Override
                    public void onNext(SearchResponse searchResponse) {
                        getView().showCafes(searchResponse.getBusinesses());
                    }
                });
    }

}
