package com.example.pawel_piedel.thesis.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.util.Util.REQUEST_PERMISSIONS_REQUEST_CODE;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

@ConfigPersistent
public class MainPresenter<V extends MainContract.View> extends BasePresenter<V> implements MainContract.Presenter<V> {
    private static final String LOG_TAG = MainPresenter.class.getName();



    @Inject
    public MainPresenter(DataManager dataManager) {
        super(dataManager);
    }

    public void managePermissions() {
        if (!checkPermissions()) {
            requestPermissions();
        }
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    public boolean checkPermissions() {
        return getView().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                getView().hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
    }


    public void requestPermissions() {
        getView().showPermissionsRequest();
    }


    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(LOG_TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                // Permission denied.
                getView().showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent settingsIntent = createSettingsIntent();
                                getView().getViewActivity().startActivity(settingsIntent);
                            }

                            @NonNull
                            private Intent createSettingsIntent() {
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                return intent;
                            }
                        });
            }
        }
    }


}
