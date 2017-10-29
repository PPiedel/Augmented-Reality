package com.example.pawel_piedel.thesis.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.Observer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.util.Util.REQUEST_PERMISSIONS_REQUEST_CODE;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

@ConfigPersistent
public class MainPresenter<V extends MainContract.View> extends BasePresenter<V> implements MainContract.Presenter<V> {
    private final static String LOG_TAG = MainPresenter.class.getName();
    public final static int REQUEST_CHECK_SETTINGS = 0;

    private RxPermissions rxPermissions;

    @Inject
    public MainPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void onFabClick() {
        rxPermissions = new RxPermissions(getView().getViewActivity());
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        getDataManager().addClosestPlacesToAugumentedRealityPlaces();
                        // Log.d(LOG_TAG,getDataManager().getAugumentedRealityPlaces().toString());
                        getView().startArActivity();
                    }
                });
    }

    @Override
    public void manageLocationSettings() {
        getDataManager().getLocationSettingsResult()
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
        return getDataManager().getFromSharedPreferences(id, true);
    }

}
