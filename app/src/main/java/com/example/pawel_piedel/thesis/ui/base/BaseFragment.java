package com.example.pawel_piedel.thesis.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.pawel_piedel.thesis.injection.components.ActivityComponent;

import butterknife.Unbinder;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */

public class BaseFragment extends android.support.v4.app.Fragment implements BaseView {

    private BaseActivity baseActivity;
    private Unbinder mUnBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.baseActivity = activity;
           // activity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        baseActivity = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    public BaseActivity getBaseActivity() {
        return baseActivity;
    }

    public ActivityComponent getActivityComponent() {
        if (baseActivity != null) {
            return baseActivity.getActivityComponent();
        }
        return null;
    }

    @Override
    public void showSnackbar(int mainTextStringId, int actionStringId, View.OnClickListener listener) {

    }

    @Override
    public void showLocationPermissionsRequest() {
        if (baseActivity !=null){
            baseActivity.showLocationPermissionsRequest();
        }
    }

    @Override
    public void showCameraPermissionRequest() {
        if (baseActivity !=null){
            baseActivity.showCameraPermissionRequest();
        }
    }

    @Override
    public void requestRequiredPermissions(String[] permissions, int requestCode) {
        if (baseActivity !=null){
            baseActivity.requestRequiredPermissions(permissions,requestCode);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        boolean has = false;
        if (baseActivity !=null){
            has = baseActivity.hasPermission(permission);
        }
        return has;
    }

}
