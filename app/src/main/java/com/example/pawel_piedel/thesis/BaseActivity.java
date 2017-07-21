package com.example.pawel_piedel.thesis;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.pawel_piedel.thesis.injection.components.ActivityComponent;
import com.example.pawel_piedel.thesis.injection.components.ConfigPersistentComponent;
import com.example.pawel_piedel.thesis.injection.components.DaggerConfigPersistentComponent;
import com.example.pawel_piedel.thesis.injection.modules.ActivityModule;

import butterknife.Unbinder;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */

public class BaseActivity extends AppCompatActivity implements BaseView, BaseFragment.Callback {
    private ActivityComponent activityComponent;
    private long mActivityId;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigPersistentComponent configPersistentComponent = DaggerConfigPersistentComponent.builder()
                .applicationComponent(ThesisApplication.get(this).getComponent())
                .build();
        activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    @Override
    public void setPresenter(Presenter deliveriesPresenter) {

    }


    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    public void setUnBinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    @TargetApi(Build.VERSION_CODES.M)
    void requestLocationPermissions(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }


}
