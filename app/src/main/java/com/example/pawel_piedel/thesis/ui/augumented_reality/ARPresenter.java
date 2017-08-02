/*
 * Based on https://github.com/googlesamples/android-Camera2Basic/blob/master/Application/src/main/java/com/example/android/camera2basic/Camera2BasicFragment.java
 *
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.augumented_reality.AzimuthManager;
import com.example.pawel_piedel.thesis.data.model.Coordinates;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.example.pawel_piedel.thesis.ui.detail.DetailActivity;
import com.example.pawel_piedel.thesis.util.Util;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.pawel_piedel.thesis.adapters.BusinessAdapter.BUSINESS;

/**
 * Created by Pawel_Piedel on 24.07.2017.
 */
@ConfigPersistent
public class ARPresenter<V extends ARContract.View> extends BasePresenter<V> implements ARContract.Presenter<V> {
    public static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int MAX_PREVIEW_WIDTH = 1920;
    private static final int MAX_PREVIEW_HEIGHT = 1080;
    private static final float ALPHA = 0.4f;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private final static String LOG_TAG = ARPresenter.class.getSimpleName();
    private String cameraId;
    private int mSensorOrientation;
    private CameraDevice cameraDevice;
    private Size imageDimension;
    private Size mPreviewSize;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
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


    private static final int AZIMUTH_ACCURACY = 10;
    private AzimuthManager azimuthManager;
    private int sensorType = Sensor.TYPE_ROTATION_VECTOR;
    private ReactiveSensors reactiveSensors;
    private double deviceAzimuth = 0;
    private Subscription azimuthSubscription;
    private Subscription locationSubscription;
    private double[] azimuths;
    private int i = 0;
    private boolean pointsTo = false;
    private float[] output = {0, 0, 0, 0, 0};

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    @Inject
    public ARPresenter(DataManager dataManager) {
        super(dataManager);
        //lastLocation = Util.mLastLocation;
        azimuths = new double[getDataManager().getRestaurants().size()];
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void startObservingAzimuth() {
        if (reactiveSensors.hasSensor(sensorType)) {
            azimuthSubscription = azimuthManager.getReactiveSensorEvents()
                    .doOnNext(reactiveSensorEvent -> {
                        pointsTo = false;
                        deviceAzimuth = calculateNewDeviceAzimuth(reactiveSensorEvent);
                        //Log.i(LOG_TAG, "Device azimuth : " + deviceAzimuth);
                        for (int i = 0; i < azimuths.length && !pointsTo; i++) {
                            if (newAzimuthPointsTo(azimuths[i])) {
                                this.pointsTo = true;
                                this.i = i;
                            }
                        }
                    })
                    //.filter(reactiveSensorEvent -> pointsTo)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ReactiveSensorEvent>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            // Log.i(LOG_TAG, throwable.getMessage());
                            throwable.printStackTrace();
                            getView().showToast("Sorry, something went wrong.");

                        }

                        @Override
                        public void onNext(ReactiveSensorEvent reactiveSensorEvent) {
                            getView().setAzimuthText(deviceAzimuth);
                            Log.i(LOG_TAG, "" + pointsTo);
                            if (pointsTo) {
                                getView().showBusinessOnScreen(getDataManager().getRestaurants().get(i));
                            } else {
                                getView().hideBusiness();
                            }


                        }
                    });
        } else {
            Log.e(LOG_TAG, "Device does not has sensor !");
        }
    }


    private boolean newAzimuthPointsTo(double businessAzimuth) {
        double minAngle = businessAzimuth - AZIMUTH_ACCURACY;
        double maxAngle = businessAzimuth + AZIMUTH_ACCURACY;

        if (minAngle < 0) {
            minAngle += 360;
        }


        if (maxAngle >= 360) {
            maxAngle -= 360;
        }
        return isBetween(minAngle, maxAngle, deviceAzimuth);
    }

    /*Based on https://github.com/lycha/augmented-reality-example/blob/master/app/src/main/java/com/lycha/example/augmentedreality/CameraViewActivity.java*/
    private Boolean isBetween(double minAngle, double maxAngle, double deviceAzimuth) {
        if (minAngle > maxAngle) {
            if (isBetween(0, maxAngle, deviceAzimuth) && isBetween(minAngle, 360, deviceAzimuth))
                return true;
        } else {
            if (deviceAzimuth > minAngle && deviceAzimuth < maxAngle)
                return true;
        }
        return false;
    }

    private int calculateNewDeviceAzimuth(ReactiveSensorEvent reactiveSensorEvent) {
        SensorEvent event = reactiveSensorEvent.getSensorEvent();
        output = lowPass(event.values,output);
        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, output);

        float[] remappedRotationMatrix = new float[16];
        SensorManager.remapCoordinateSystem(rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remappedRotationMatrix);

        float[] orientation = new float[3];
        return (int) (Math.toDegrees(SensorManager.getOrientation(remappedRotationMatrix, orientation)[0]) + 360) % 360;


        /*output = lowPass(event.values, output);
        Log.i(LOG_TAG, Arrays.toString(output));
        float[] orientation = new float[3];
        float[] rMat = new float[9];
        SensorManager.getRotationMatrixFromVector(rMat, output);
        return (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;*/
    }

    protected float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }


    public void startObservingLocation() {
        if (Util.mLastLocation != null) {
            getView().setLocationText(Util.mLastLocation);
            updateBusinessAzimuths(Util.mLastLocation);
        }
        locationSubscription = getDataManager().getLocationUpdates()
                .subscribeOn(Schedulers.io())
                .filter(location -> locationsAreDiffrent(location, Util.mLastLocation))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Location>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(LOG_TAG, e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Location location) {
                        Log.i(LOG_TAG, location.toString());

                        getDataManager().saveLocation(location);
                        getView().setLocationText(location);
                        updateBusinessAzimuths(location);
                    }
                });
    }

    @Override
    public void openDetailActivity() {

        Intent detailActivityIntent = new Intent(getView().getViewActivity(), DetailActivity.class);
        detailActivityIntent.putExtra(BUSINESS, getDataManager().getRestaurants().get(i));
        getView().getViewActivity().startActivity(detailActivityIntent);
    }

    private boolean locationsAreDiffrent(Location first, Location second) {
        return first.getLatitude() != second.getLatitude() && first.getLongitude() != second.getLongitude();
    }

    public void updateBusinessAzimuths(Location currentLocation) {
        if (!getDataManager().getRestaurants().isEmpty()) {
            for (int i = 0; i < azimuths.length; i++) {
                azimuths[i] = calculateTeoreticalAzimuth(getDataManager().getRestaurants().get(i).getCoordinates(), currentLocation);
            }
            Log.i(LOG_TAG, Arrays.toString(azimuths));
        }

    }

    /*Based on https://github.com/lycha/augmented-reality-example/blob/master/app/src/main/java/com/lycha/example/augmentedreality/CameraViewActivity.java*/
    public double calculateTeoreticalAzimuth(Coordinates coordinates, Location currentLocation) {
        double dX = coordinates.getLatitude() - currentLocation.getLatitude();
        double dY = coordinates.getLongitude() - currentLocation.getLongitude();

        double czwartak;
        double tangensCzwartaka;

        tangensCzwartaka = Math.abs(dY / dX);
        czwartak = Math.atan(tangensCzwartaka);
        czwartak = Math.toDegrees(czwartak);

        if (dX > 0 && dY > 0) { // I quater
            return czwartak;
        } else if (dX < 0 && dY > 0) { // II
            return 180 - czwartak;
        } else if (dX < 0 && dY < 0) { // III
            return 180 + czwartak;
        } else if (dX > 0 && dY < 0) { // IV
            return 360 - czwartak;
        }

        return czwartak;
    }

    @Override
    public void unsubscribeAll() {
        azimuthManager.safelyUnsubscribe(azimuthSubscription);
        getDataManager().safelyUnsubscribe(locationSubscription);
    }

    @Override
    public void setReactiveSensors(Context context) {
        reactiveSensors = new ReactiveSensors(context);
        azimuthManager = new AzimuthManager(reactiveSensors, sensorType);
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

    @SuppressLint("MissingPermission")
    @Override
    public void openCamera(int width, int height) {
        CameraManager manager = (CameraManager) getView().getViewActivity().getSystemService(Context.CAMERA_SERVICE);
        Log.e(LOG_TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];

            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            setUpCameraOutputs(characteristics, width, height);
            configureTransform(width, height);
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
        getView().showCameraPreview(imageDimension, mBackgroundHandler, cameraDevice);
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

    public void setUpCameraOutputs(CameraCharacteristics characteristics, int width, int height) {
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Size largest = Collections.max(
                Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                new CompareSizesByArea());
        // Find out if we need to swap dimension to get the preview size relative to sensor
        // coordinate.
        int displayRotation = getView().getViewActivity().getWindowManager().getDefaultDisplay().getRotation();
        //noinspection ConstantConditions
        mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        boolean swappedDimensions = false;
        switch (displayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                    swappedDimensions = true;
                }
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                    swappedDimensions = true;
                }
                break;
            default:
                Log.e(LOG_TAG, "Display rotation is invalid: " + displayRotation);
        }

        Point displaySize = new Point();
        getView().getViewActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);
        int rotatedPreviewWidth = width;
        int rotatedPreviewHeight = height;
        int maxPreviewWidth = displaySize.x;
        int maxPreviewHeight = displaySize.y;

        if (swappedDimensions) {
            rotatedPreviewWidth = height;
            rotatedPreviewHeight = width;
            maxPreviewWidth = displaySize.y;
            maxPreviewHeight = displaySize.x;
        }

        if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
            maxPreviewWidth = MAX_PREVIEW_WIDTH;
        }

        if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
            maxPreviewHeight = MAX_PREVIEW_HEIGHT;
        }

        // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
        // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
        // garbage capture data.
        mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                maxPreviewHeight, largest);

        // We fit the aspect ratio of TextureView to the size of preview we picked.
        int orientation = getView().getViewActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getView().setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        } else {
            getView().setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
        }


    }

    public void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getView().getViewActivity();
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }

        getView().setTransform(matrix);
    }


    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(LOG_TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

}
