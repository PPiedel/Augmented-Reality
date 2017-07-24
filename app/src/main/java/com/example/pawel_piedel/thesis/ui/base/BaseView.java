package com.example.pawel_piedel.thesis.ui.base;

import android.view.View;

import com.example.pawel_piedel.thesis.data.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface BaseView<T extends Presenter> {

    void showSnackbar(int mainTextStringId, int actionStringId, View.OnClickListener listener);

    void showLocationPermissionsRequest();

    void showCameraPermissionRequest();

    void requestRequiredPermissions(String[] permissions, int requestCode);

    boolean hasPermission(String permission);
}
