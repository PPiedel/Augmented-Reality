package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.hardware.camera2.CameraDevice;
import android.os.Handler;
import android.util.Size;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;

import rx.Subscription;

/**
 * Created by Pawel_Piedel on 24.07.2017.
 */

public interface ARContract {
    public interface View extends BaseView {
        Activity getViewActivity();

        void showCameraPreview(Size imageDimension, Handler mBackgroundHandler, CameraDevice cameraDevice);

        void setTransform(Matrix matrix);

        void showToast(String message);

        void setAzimuthText(int azimuth);

        void setAspectRatio(int x, int y);
    }

    public interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {
        void openCamera(int width, int height);

        void createCameraPreview();

        void stopBackgroundThread();

        void startBackgroundThread();

        void configureTransform(int viewWidth, int viewHeight);

        void startObservingSensor();

        void stopObservingSensor();

        void setReactiveSensors(Context context);


    }
}
