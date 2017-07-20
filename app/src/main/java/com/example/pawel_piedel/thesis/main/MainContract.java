package com.example.pawel_piedel.thesis.main;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.pawel_piedel.thesis.BasePresenter;
import com.example.pawel_piedel.thesis.BaseView;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface MainContract {
    public interface View extends BaseView<Presenter> {
        void showSnackbar(final int mainTextStringId,
                          final int actionStringId,
                          android.view.View.OnClickListener listener);

        void startLocationPermissionRequest();
        void showPermissionsRequest();
        Activity getViewActivity();
        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                        @NonNull int[] grantResults);
    }


    public interface Presenter extends BasePresenter {
        boolean checkPermissions();
        void requestPermissions();
        void onPermissionResult(int requestCode, @NonNull String[] permissions,
                                @NonNull int[] grantResults);
    }
}
