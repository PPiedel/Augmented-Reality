package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.hardware.camera2.CameraDevice;
import android.os.Handler;
import android.util.Size;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseView;

/**
 * Created by Pawel_Piedel on 24.07.2017.
 */

public interface ARContract {
    interface View extends BaseView {
        void showBusinessOnScreen(Business business);

        void hideBusiness();

        Activity getViewActivity();

        void showCameraPreview(Size imageDimension, Handler mBackgroundHandler, CameraDevice cameraDevice);

        void setTransform(Matrix matrix);

        void showToast(String message);

        void setAspectRatio(int x, int y);

        void startDetailActivity(Business business);

        void startObserving();
    }

    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {

        void onCameraOpened();

        void observeDeviceAzimuth();

        void unsubscribeThreeSensors(boolean azimuth, boolean accuracy, boolean location, boolean gravity);

        void setReactiveSensors(Context context);

        void observeDeviceLocation();

        void openDetailActivity();

        void startObservingSensors();

        void managePermissions();

        void observeGravitySensor();

        void closeCamera();

        void stopBackgroundThread();

        void onViewCreated(V view);

    }
}