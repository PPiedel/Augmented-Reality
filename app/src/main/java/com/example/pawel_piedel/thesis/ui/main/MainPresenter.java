package com.example.pawel_piedel.thesis.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import static com.example.pawel_piedel.thesis.util.Util.REQUEST_PERMISSIONS_REQUEST_CODE;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

@ConfigPersistent
public class MainPresenter<V extends MainContract.View> extends BasePresenter<V> implements MainContract.Presenter<V> {
    private static final String LOG_TAG = MainPresenter.class.getName();

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

}
