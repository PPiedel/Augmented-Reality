package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraDevice;
import android.location.Location;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.model.Coordinates;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;
import com.tbruyelle.rxpermissions.RxPermissions;

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
    private static final float ALPHA_PARAM = 0.3f;
    private static final int AZIMUTH_ACCURACY = 12;
    private static final double PITCH_ACCURACY = 0.7;
    private static final double PI_DIVIDED_BY_TWO = Math.PI / 2;
    private static final int Z_AXIS = 0;

    private ReactiveSensors reactiveSensors;
    private int deviceAzimuth = 0;
    private float gravity = 0;
    private float[] gravityVector = new float[3];
    private float groundDeviation;
    private boolean turnedOn = true;

    private Subscription azimuthSubscription;
    private Subscription accuracySubscription;
    private Subscription locationSubscription;
    private Subscription gravitySubscription;
    private double[] azimuths;
    private int i = 0;
    private boolean pointsTo = false;
    private float[] rotationMatrix = new float[16];
    private float[] remappedRotationMatrix = new float[16];
    private float[] zOrientation = new float[3];

    private float[] output = {0, 0, 0, 0, 0};
    private ReactiveSensorManager reactiveSensorManager;
    private boolean showHighAzimuthAccuracyAlert = false;

    @Inject
    CameraManager cameraManager;


    @Inject
    public ARPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        azimuths = new double[getDataManager().getAugumentedRealityPlaces().size()];
    }

    @Override
    public void startObservingSensors() {
        observeDeviceAzimuth();
        observeDeviceLocation();
        observeDeviceAzimuthAccuracy();
    }

    @Override
    public void observeDeviceAzimuth() {
        if (reactiveSensors.hasSensor(Sensor.TYPE_ROTATION_VECTOR)) {
            azimuthSubscription = reactiveSensorManager.getReactiveSensorEvents(Sensor.TYPE_ROTATION_VECTOR, SensorManager.SENSOR_DELAY_NORMAL)
                    .doOnNext(reactiveSensorEvent -> {
                        pointsTo = false;
                        deviceAzimuth = calculateDevicePosition(reactiveSensorEvent, Z_AXIS);
                        checkPlacesAgainstNewAzimuth();
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
                            if (pointsTo) {
                                getView().showBusinessOnScreen(getDataManager().getAugumentedRealityPlaces().get(i));
                            } else {
                                getView().hideBusiness();
                            }
                        }
                    });
        } else {
            getView().showToast("Device does not has required sensor !");
            Log.e(LOG_TAG, "Device does not has required sensor !");
        }
    }

    private void checkPlacesAgainstNewAzimuth() {
        for (int i = 0; i < azimuths.length && !pointsTo; i++) {
            if (newAzimuthPointsTo(azimuths[i])) {
                this.pointsTo = true;
                this.i = i;
            }
        }
    }

    public void observeDeviceAzimuthAccuracy() {
        if (reactiveSensors.hasSensor(Sensor.TYPE_ROTATION_VECTOR)) {
            accuracySubscription = reactiveSensorManager.getReactiveSensorAccuracy(Sensor.TYPE_ROTATION_VECTOR)
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ReactiveSensorEvent>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ReactiveSensorEvent reactiveSensorEvent) {
                            switch (reactiveSensorEvent.getAccuracy()) {
                                case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                                    getView().showAlert("Niski stopień dokładności wskazań kierunku urządzenia", "Dokładność wskazań kierunku Twojego urządzenia jest niska. Wskazania nie będą dokładne. Aby poprawić jakość wskazań wykonaj kilka ósemek urządzeniem w powietrzu.");
                                    showHighAzimuthAccuracyAlert = true;
                                    break;
                                case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                                    getView().showAlert("Sredni stopień dokładności wskazań kierunku urządzenia", "Dokładność wskazań kierunku Twojego urządzenia jest średnia. Wskazania mogą nie być dokładne. Aby poprawić jakość wskazań wykonaj kilka ósemek urządzeniem w powietrzu.");
                                    showHighAzimuthAccuracyAlert = true;
                                    break;
                                case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                                    if (showHighAzimuthAccuracyAlert) {
                                        getView().showAlert("Wysoki stopień dokładności wskazań kierunku urządzenia", "Dokładność wskazań kierunku Twojego urządzenia jest wysoka. Zamknij komunikat i ciesz się rozszerzoną rzeczywistością :) !");
                                    }
                                    showHighAzimuthAccuracyAlert = false;
                                    break;
                            }

                        }
                    });
        } else {
            getView().showToast("Device does not has required sensor !");
            Log.e(LOG_TAG, "Device does not has required sensor !");
        }
    }

    public void observeGravitySensor() {
        if (reactiveSensors.hasSensor(Sensor.TYPE_GRAVITY)) {
            gravitySubscription = reactiveSensorManager.getReactiveSensorEvents(Sensor.TYPE_GRAVITY, SensorManager.SENSOR_DELAY_NORMAL)
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ReactiveSensorEvent>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().showToast(e.getMessage());
                        }

                        @Override
                        public void onNext(ReactiveSensorEvent reactiveSensorEvent) {
                            groundDeviation = calculateGravity(reactiveSensorEvent);
                            if (deviceIsFlat()) {
                                onDeviceIsFlat();
                            } else if (deviceIsVertical()) {
                                onDeviceIsVertical();
                            }
                        }
                    });

        }
    }

    private boolean deviceIsVertical() {
        return !turnedOn && groundDeviation > PI_DIVIDED_BY_TWO - PITCH_ACCURACY && groundDeviation < PI_DIVIDED_BY_TWO + PITCH_ACCURACY;
    }

    private boolean deviceIsFlat() {
        return turnedOn && (groundDeviation < PI_DIVIDED_BY_TWO - PITCH_ACCURACY || groundDeviation > PI_DIVIDED_BY_TWO + PITCH_ACCURACY);
    }

    private void onDeviceIsFlat() {
        unsubscribeThreeSensors(true, true, true, false);
        getView().hideBusiness();
        turnedOn = false;
        getView().showToast("Umieść swoje urządzenie pionowo, aby znów zacząć korzystać z trybu rozszerzonej rzeczywistości.");
    }

    private void onDeviceIsVertical() {
        startObservingSensors();
        turnedOn = true;
        getView().showToast("Znów korzystasz z trybu rozszerzonej rzeczywistości.");
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

    private float calculateGravity(ReactiveSensorEvent reactiveSensorEvent) {
        System.arraycopy(reactiveSensorEvent.getSensorEvent().values, 0, gravityVector, 0, gravityVector.length);
        gravity = (float) Math.sqrt(gravityVector[0] * gravityVector[0] + gravityVector[1] * gravityVector[1] + gravityVector[2] * gravityVector[2]);

        for (int i = 0; i < gravityVector.length; i++) {
            gravityVector[i] /= gravity;
        }

        return (float) Math.acos(gravityVector[2]);

    }

    /*Based on https://github.com/lycha/augmented-reality-example/blob/master/app/src/main/java/com/lycha/example/augmentedreality/CameraViewActivity.java*/
    private int calculateDevicePosition(ReactiveSensorEvent reactiveSensorEvent, int axis) {
        SensorEvent event = reactiveSensorEvent.getSensorEvent();
        output = lowPassFilter(event.values, output);
        SensorManager.getRotationMatrixFromVector(rotationMatrix, output);


        SensorManager.remapCoordinateSystem(rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remappedRotationMatrix);


        return (int) (Math.toDegrees(SensorManager.getOrientation(remappedRotationMatrix, zOrientation)[axis]) + 360) % 360;

    }

    private float[] lowPassFilter(float[] input, float[] output) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA_PARAM * (input[i] - output[i]);
        }
        return output;
    }

    public void observeDeviceLocation() {
        if (getDataManager().getLastLocation() != null) {
            // getView().setLocationText(getDataManager().getLastLocation());
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
                        getDataManager().setLastLocation(location);
                        updateBusinessAzimuths(location);
                    }
                });
    }

    @Override
    public void openDetailActivity() {
        getView().startDetailActivity(getDataManager().getAugumentedRealityPlaces().get(i));
    }

    private boolean locationsAreDifferent(Location first, Location second) {
        return first.getLatitude() != second.getLatitude() && first.getLongitude() != second.getLongitude();
    }

    private void updateBusinessAzimuths(Location currentLocation) {
        if (!getDataManager().getAugumentedRealityPlaces().isEmpty()) {
            for (int i = 0; i < azimuths.length; i++) {
                azimuths[i] = calculateTeoreticalAzimuth(getDataManager().getAugumentedRealityPlaces().get(i).getCoordinates(), currentLocation);
            }
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
    public void unsubscribeThreeSensors(boolean azimuth, boolean accuracy, boolean location, boolean gravity) {
        reactiveSensorManager.unsubscribe(azimuthSubscription);
        reactiveSensorManager.unsubscribe(accuracySubscription);
        getDataManager().safelyUnsubscribe(locationSubscription);

        if (gravity) {
            reactiveSensorManager.unsubscribe(gravitySubscription);
        }
    }


    @Override
    public void setReactiveSensors(Context context) {
        reactiveSensors = new ReactiveSensors(context);
        reactiveSensorManager = new ReactiveSensorManager(reactiveSensors);
    }


    public void managePermissions() {
        RxPermissions rxPermissions = new RxPermissions(getView().getViewActivity());
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        getView().startObserving();
                    }
                });
    }


    public void setCameraDevice(CameraDevice cameraDevice) {
        cameraManager.setCameraDevice(cameraDevice);
    }

    @Override
    public void onCameraOpened() {
        getView().showCameraPreview(cameraManager.getImageDimension(), cameraManager.getmBackgroundHandler(), cameraManager.getCameraDevice());
    }

    public void openCamera(int width, int height, ARActivity activity) {
        cameraManager.openCamera(width, height, activity);
    }

    public void closeCamera() {
        cameraManager.closeCamera();
    }

    public void configureTransform(int width, int height) {
        cameraManager.configureTransform(width, height, (ARActivity) getView());
    }

    public void startCameraBackgroundThread() {
        cameraManager.startBackgroundThread();
    }

    public void stopBackgroundThread() {
        cameraManager.stopBackgroundThread();
    }

    public void setStateCallback(CameraDevice.StateCallback stateCallback) {
        cameraManager.setStateCallback(stateCallback);
    }

}