package com.example.pawel_piedel.thesis.ui.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.ThesisApplication;
import com.example.pawel_piedel.thesis.injection.components.ActivityComponent;
import com.example.pawel_piedel.thesis.injection.components.ConfigPersistentComponent;
import com.example.pawel_piedel.thesis.injection.components.DaggerConfigPersistentComponent;
import com.example.pawel_piedel.thesis.injection.modules.ActivityModule;

import butterknife.Unbinder;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 * Based on https://github.com/MindorksOpenSource/android-dagger2-example
 * and https://github.com/ribot/android-boilerplate
 * and https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger/
 */

public class BaseActivity extends AppCompatActivity implements BaseView {
    private final String LOG_TAG = BaseActivity.class.getSimpleName();
    private ActivityComponent activityComponent;
    private Unbinder unbinder;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigPersistentComponent configPersistentComponent = DaggerConfigPersistentComponent.builder()
                .applicationComponent(ThesisApplication.get(this).getComponent())
                .build();
        activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));

    }

    public Unbinder getUnbinder() {
        return unbinder;
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    protected void setUnBinder(Unbinder unBinder) {
        this.unbinder = unBinder;
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void showProgressDialog() {
        hideProgressDialog();

        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void showAlert(String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
