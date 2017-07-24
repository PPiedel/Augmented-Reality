package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.View;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;

import javax.inject.Inject;

import static com.example.pawel_piedel.thesis.util.Util.REQUEST_PERMISSIONS_REQUEST_CODE;

/**
 * Created by Pawel_Piedel on 24.07.2017.
 */
@ConfigPersistent
public class ARPresenter<V extends ARContract.View> extends BasePresenter<V> implements ARContract.Presenter<V> {
    public static final int REQUEST_CAMERA_PERMISSION = 200;
    private final String LOG_TAG = ARPresenter.class.getSimpleName();
    private String cameraId;
    protected CameraDevice cameraDevice;
    private Size imageDimension;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(LOG_TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };


    @Inject
    public ARPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void managePermissions() {
        if (!checkPermissions()) {
            requestPermissions();
        }
    }

    public boolean checkPermissions() {
        return getView().hasPermission(Manifest.permission.CAMERA);
    }


    public void requestPermissions() {
        getView().showCameraPermissionRequest();
    }

    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
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

    @Override
    public void openCamera() {
        CameraManager manager = (CameraManager) getView().getViewActivity().getSystemService(Context.CAMERA_SERVICE);
        Log.e(LOG_TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (!checkPermissions()) {
                requestPermissions();
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(LOG_TAG, "openCamera X");
    }

    public void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    @Override
    public void createCameraPreview() {
        getView().showCameraPreview(imageDimension,mBackgroundHandler,cameraDevice);
    }

    @Override
    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    public void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
