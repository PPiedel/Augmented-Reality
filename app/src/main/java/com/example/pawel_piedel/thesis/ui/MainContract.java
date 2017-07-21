package com.example.pawel_piedel.thesis.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.pawel_piedel.thesis.BaseView;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface MainContract {
    public interface View extends BaseView {

        void showSnackbar(int mainTextStringId, int actionStringId, android.view.View.OnClickListener listener);

        void requestLocationPermissions();

        void showPermissionsRequest();

        Activity getViewActivity();

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                        @NonNull int[] grantResults);
    }


    public interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.Presenter<V> {
        boolean checkPermissions();

        void requestPermissions();

        void onPermissionResult(int requestCode, @NonNull String[] permissions,
                                @NonNull int[] grantResults);
    }
}
