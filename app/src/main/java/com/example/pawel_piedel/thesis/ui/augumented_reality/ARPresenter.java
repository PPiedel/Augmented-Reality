package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraDevice;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.model.Coordinates;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;

import java.util.Arrays;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

/**
 * Created by Pawel_Piedel on 24.07.2017.
 */
@ConfigPersistent
public class ARPresenter<V extends ARContract.View> extends BasePresenter<V> implements ARContract.Presenter<V> {
    private final static String LOG_TAG = ARPresenter.class.getSimpleName();
    public static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final float ALPHA = 0.4f;
    private static final int AZIMUTH_ACCURACY = 10;

    private final int sensorType = Sensor.TYPE_ROTATION_VECTOR;
    private ReactiveSensors reactiveSensors;
    private double deviceAzimuth = 0;
    private Subscription azimuthSubscription;
    private Subscription locationSubscription;
    private double[] azimuths;
    private int i = 0;
    private boolean pointsTo = false;
    private float[] output = {0, 0, 0, 0, 0};

    @Inject
    CameraManager cameraManager;

    private AzimuthManager azimuthManager;


    @Inject
    public ARPresenter(DataManager dataManager) {
        super(dataManager);
        //lastLocation = Util.lastLocation;
        azimuths = new double[getDataManager().getRestaurants().size()];

    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void startObservingAzimuth() {
        if (reactiveSensors.hasSensor(sensorType)) {
            azimuthSubscription = azimuthManager.getReactiveSensorEvents()
                    .doOnNext(reactiveSensorEvent -> {
                        pointsTo = false;
                        deviceAzimuth = calculateNewDeviceAzimuth(reactiveSensorEvent);

                        for (int i = 0; i < azimuths.length && !pointsTo; i++) {
                            if (newAzimuthPointsTo(azimuths[i])) {
                                this.pointsTo = true;
                                this.i = i;
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ReactiveSensorEvent>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                            getView().showToast(throwable.getMessage());

                        }

                        @Override
                        public void onNext(ReactiveSensorEvent reactiveSensorEvent) {
                            getView().setAzimuthText(deviceAzimuth);
                            Log.d(LOG_TAG,""+deviceAzimuth);
                            if (pointsTo) {
                                getView().showBusinessOnScreen(getDataManager().getRestaurants().get(i));
                            } else {
                                getView().hideBusiness();
                            }


                        }
                    });
        } else {

            Log.e(LOG_TAG, "Device does not has required sensor !");
        }
    }

    /*Based on https://github.com/lycha/augmented-reality-example/blob/master/app/src/main/java/com/lycha/example/augmentedreality/CameraViewActivity.java*/
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

    /*Based on https://github.com/lycha/augmented-reality-example/blob/master/app/src/main/java/com/lycha/example/augmentedreality/CameraViewActivity.java*/
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

    }

    private float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    public void startObservingLocation() {
        if (getDataManager().getLastLocation() != null) {
            getView().setLocationText(getDataManager().getLastLocation());
            updateBusinessAzimuths(getDataManager().getLastLocation());
        }
        locationSubscription = getDataManager().getLocationUpdates()
                .subscribeOn(Schedulers.io())
                .filter(location -> locationsAreDifferent(location, getDataManager().getLastLocation()))
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

                        getDataManager().setLastLocation(location);
                        getView().setLocationText(location);
                        updateBusinessAzimuths(location);
                    }
                });
    }

    @Override
    public void openDetailActivity() {

        getView().startDetailActivity(getDataManager().getRestaurants().get(i));
    }

    private boolean locationsAreDifferent(Location first, Location second) {
        return first.getLatitude() != second.getLatitude() && first.getLongitude() != second.getLongitude();
    }

    private void updateBusinessAzimuths(Location currentLocation) {
        if (!getDataManager().getRestaurants().isEmpty()) {
            for (int i = 0; i < azimuths.length; i++) {
                azimuths[i] = calculateTeoreticalAzimuth(getDataManager().getRestaurants().get(i).getCoordinates(), currentLocation);
            }
            Log.i(LOG_TAG, Arrays.toString(azimuths));
        }

    }

    /*Based on https://github.com/lycha/augmented-reality-example/blob/master/app/src/main/java/com/lycha/example/augmentedreality/CameraViewActivity.java*/
    private double calculateTeoreticalAzimuth(Coordinates coordinates, Location currentLocation) {
        double dX = coordinates.getLatitude() - currentLocation.getLatitude();
        double dY = coordinates.getLongitude() - currentLocation.getLongitude();

        double czwartak;
        double tangensCzwartaka;

        tangensCzwartaka = Math.abs(dY / dX);
        czwartak = Math.atan(tangensCzwartaka);
        czwartak = Math.toDegrees(czwartak);

        if (dX > 0 && dY > 0) {
            return czwartak;
        } else if (dX < 0 && dY > 0) {
            return 180 - czwartak;
        } else if (dX < 0 && dY < 0) {
            return 180 + czwartak;
        } else if (dX > 0 && dY < 0) {
            return 360 - czwartak;
        }

        return czwartak;
    }

    @Override
    public void unsubscribeAll() {
        azimuthManager.unsubscribe(azimuthSubscription);
        getDataManager().safelyUnsubscribe(locationSubscription);
    }

    @Override
    public void setReactiveSensors(Context context) {
        reactiveSensors = new ReactiveSensors(context);
        azimuthManager = new AzimuthManager(reactiveSensors, sensorType);
    }


    public void managePermissions() {
        if (checkPermissions()) {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return !getView().hasPermission(Manifest.permission.CAMERA);
    }


    private void requestPermissions() {
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

    public void setCameraDevice(CameraDevice cameraDevice){
        cameraManager.setCameraDevice(cameraDevice);
    }

    @Override
    public void onCameraOpened() {
        getView().showCameraPreview(cameraManager.getImageDimension(), cameraManager.getmBackgroundHandler(), cameraManager.getCameraDevice());
    }

    public void openCamera(int width, int height,ARActivity activity){
        cameraManager.openCamera(width,height,activity);
    }

    public void closeCamera(){
        cameraManager.closeCamera();
    }

    public void configureTransform(int width,int height){
        cameraManager.configureTransform(width,height,(ARActivity) getView());
    }

    public void startCameraBackgroundThread(){
        cameraManager.startBackgroundThread();
    }

    public void stopBackgroundThread(){
        cameraManager.stopBackgroundThread();
    }

    public void setStateCallback(CameraDevice.StateCallback stateCallback){
        cameraManager.setStateCallback(stateCallback);
    }

    public void setI(int i) {
        this.i = i;
    }
}
