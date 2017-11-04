package com.example.pawel_piedel.thesis.ui.main;

import android.Manifest;
import android.content.IntentSender;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.BusinessDataSource;
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
    public MainPresenter(BusinessDataSource businessDataSource) {
        super(businessDataSource);
    }

    @Override
    public void onFabClick() {
        rxPermissions = new RxPermissions(getView().getViewActivity());
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        getBusinessDataSource().addClosestPlacesToAugumentedRealityPlaces();
                        // Log.d(LOG_TAG,getBusinessDataSource().getAugumentedRealityPlaces().toString());
                        getView().startArActivity();
                    }
                });
    }

    @Override
    public void manageLocationSettings() {
        getBusinessDataSource().getLocationSettingsResult()
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
                                    status.startResolutionForResult(getView().getViewActivity(), REQUEST_CHECK_SETTINGS);
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
        getView().init();
    }

    @Override
    public boolean isPlaceSaved(String id) {
        return getBusinessDataSource().getFromSharedPreferences(id, true);
    }

}
