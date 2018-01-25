package com.example.pawel_piedel.thesis.ui.main;

import android.Manifest;
import android.content.IntentSender;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.auth.AccessTokenRepository;
import com.example.pawel_piedel.thesis.data.business.BusinessRepository;
import com.example.pawel_piedel.thesis.data.location.LocationRepository;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

@ConfigPersistent
public class MainPresenter<V extends MainContract.View> extends BasePresenter<V> implements MainContract.Presenter<V> {
    public final static int REQUEST_CHECK_SETTINGS = 0;
    private final static String LOG_TAG = MainPresenter.class.getName();
    private RxPermissions rxPermissions;

    @Inject
    public MainPresenter(BusinessRepository businessRepository, LocationRepository locationRepository, AccessTokenRepository accessTokenRepository) {
        super(businessRepository, locationRepository, accessTokenRepository);
    }

    @Override
    public void onFabClick() {
        rxPermissions = new RxPermissions(view.getViewActivity());
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        businessRepository.addClosestPlacesToAugumentedRealityPlaces();
                        // Log.d(LOG_TAG,getBusinessRepository().getAugumentedRealityPlaces().toString());
                        view.startArActivity();
                    }
                });
    }

    @Override
    public void manageLocationSettings() {
        locationRepository.getLocationSettingsResult()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LocationSettingsResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LocationSettingsResult locationSettingsResult) {
                        Status status = locationSettingsResult.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    status.startResolutionForResult(view.getViewActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException th) {
                                    Log.e("MainActivity", "Error opening settings activity.", th);
                                }
                                break;
                            case LocationSettingsStatusCodes.SUCCESS:
                                onLocationPermissionsGranted();

                        }
                    }
                });
    }

    @Override
    public void onLocationPermissionsGranted() {
        view.init();
    }

    @Override
    public boolean isPlaceSaved(String id) {
        return businessRepository.getFromSharedPreferences(id, true);
    }

}
