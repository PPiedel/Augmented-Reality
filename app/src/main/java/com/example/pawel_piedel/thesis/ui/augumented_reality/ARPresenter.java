package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.Manifest;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraDevice;
import android.location.Location;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.auth.AccessTokenRepository;
import com.example.pawel_piedel.thesis.data.business.BusinessRepository;
import com.example.pawel_piedel.thesis.data.location.LocationRepository;
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
    private static final double PI_DIVIDED_BY_TWO = Math.PI / 2;
    private static final int Z_AXIS = 0;
    static float ALPHA_PARAM = 0.5f;
    static double PITCH_ACCURACY = 0.7;
    static int AZIMUTH_ACCURACY = 12;

    @Inject
    CameraManager cameraManager;

    private int deviceAzimuth = 0;
    private float gravity = 0;
    private float[] gravityVector = new float[3];
    private float groundDeviation;
    private boolean turnedOn = true;
    private Subscription azimuthSubscription;
    private Subscription accuracySubscription;
    private Subscription locationSubscription;
    private Subscription gravitySubscription;
    private double[] azimuths; //this array is matching augumented reality places, which are sorted in ascending order by distance
    private double azimuthDelta;
    private int bestMatchedPlaceIndex = 0;
    private boolean pointsTo = false;
    private float[] rotationMatrix = new float[16];
    private float[] remappedRotationMatrix = new float[16];
    private float[] zOrientation = new float[3];
    private float[] output = {0, 0, 0, 0, 0};
    private ReactiveSensorManager reactiveSensorManager;
    private boolean showHighAzimuthAccuracyAlert = false;


    @Inject
    public ARPresenter(BusinessRepository businessRepository, LocationRepository locationRepository, AccessTokenRepository accessTokenRepository) {
        super(businessRepository, locationRepository, accessTokenRepository);
    }

    @Override
    public void onViewCreated(V view) {
        attachView(view);

        setUpReactiveSensorManager((Context) view);
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        if (businessRepository.getAugumentedRealityPlaces() != null) {
            azimuths = new double[businessRepository.getAugumentedRealityPlaces().size()];
        }

    }

    @Override
    public void startObservingSensors() {
        observeDeviceAzimuth();
        observeDeviceLocation();
        observeDeviceAzimuthAccuracy();
    }

    @Override
    public void observeDeviceAzimuth() {
        if (reactiveSensorManager.hasSensor(Sensor.TYPE_ROTATION_VECTOR)) {
            azimuthSubscription = reactiveSensorManager.getReactiveSensorEvents(Sensor.TYPE_ROTATION_VECTOR, SensorManager.SENSOR_DELAY_NORMAL)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ReactiveSensorEvent>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();
                            view.showToast(throwable.getMessage());

                        }

                        @Override
                        public void onNext(ReactiveSensorEvent reactiveSensorEvent) {
                            deviceAzimuth = calculateDevicePosition(reactiveSensorEvent, Z_AXIS);
                            pointsTo = false;
                            checkPlacesAzimuthsAgainstNewAzimuth();
                            if (pointsTo) {
                                view.showBusinessOnScreen(businessRepository.getAugumentedRealityPlaces().get(bestMatchedPlaceIndex));
                            } else {
                                view.hideBusiness();
                            }
                        }
                    });
        } else {
            view.showToast("Device does not has required sensor !");
        }
    }

    public void checkPlacesAzimuthsAgainstNewAzimuth() {
        for (int placeIndex = 0; placeIndex < azimuths.length; placeIndex++) {
            if (newAzimuthPointsTo(azimuths[placeIndex], deviceAzimuth)) {
                azimuthDelta = Math.abs(azimuths[placeIndex] - deviceAzimuth);
                if (!pointsTo) {
                    pointsTo = true;
                    bestMatchedPlaceIndex = placeIndex;
                } else if (pointsTo
                        && businessRepository.getAugumentedRealityPlaces().get(placeIndex).getDistance() <= businessRepository.getAugumentedRealityPlaces().get(bestMatchedPlaceIndex).getDistance()
                        && azimuthDelta < Math.abs(azimuths[bestMatchedPlaceIndex] - deviceAzimuth)) {
                    pointsTo = true;
                    bestMatchedPlaceIndex = placeIndex;
                }
            }

        }
    }

    public void observeDeviceAzimuthAccuracy() {
        if (reactiveSensorManager.hasSensor(Sensor.TYPE_ROTATION_VECTOR)) {
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
                                    view.showAlert("Niski stopień dokładności wskazań kierunku urządzenia", "Dokładność wskazań kierunku Twojego urządzenia jest niska. Wskazania nie będą dokładne. Aby poprawić jakość wskazań wykonaj kilka ósemek urządzeniem w powietrzu.");
                                    showHighAzimuthAccuracyAlert = true;
                                    break;
                                case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                                    view.showAlert("Sredni stopień dokładności wskazań kierunku urządzenia", "Dokładność wskazań kierunku Twojego urządzenia jest średnia. Wskazania mogą nie być dokładne. Aby poprawić jakość wskazań wykonaj kilka ósemek urządzeniem w powietrzu.");
                                    showHighAzimuthAccuracyAlert = true;
                                    break;
                                case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                                    if (showHighAzimuthAccuracyAlert) {
                                        view.showAlert("Wysoki stopień dokładności wskazań kierunku urządzenia", "Dokładność wskazań kierunku Twojego urządzenia jest wysoka. Zamknij komunikat bestMatchedPlaceIndex ciesz się rozszerzoną rzeczywistością :) !");
                                    }
                                    showHighAzimuthAccuracyAlert = false;
                                    break;
                            }

                        }
                    });
        } else {
            view.showToast("Device does not has required sensor !");
            Log.e(LOG_TAG, "Device does not has required sensor !");
        }
    }

    public void observeGravitySensor() {
        if (reactiveSensorManager.hasSensor(Sensor.TYPE_GRAVITY)) {
            gravitySubscription = reactiveSensorManager.getReactiveSensorEvents(Sensor.TYPE_GRAVITY, SensorManager.SENSOR_DELAY_NORMAL)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ReactiveSensorEvent>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showToast(e.getMessage());
                        }

                        @Override
                        public void onNext(ReactiveSensorEvent reactiveSensorEvent) {
                            groundDeviation = calculateGravity(reactiveSensorEvent);
                            if (deviceIsFlatWhileTurnedOn()) {
                                onDeviceIsFlat();
                            } else if (deviceIsVerticalWhileTurnedOff()) {
                                onDeviceIsVertical();
                            }
                        }
                    });

        }
    }

    public void observeDeviceLocation() {
       /* if (locationRepository.getLastKnownLocation() != null) {
            updateBusinessAzimuths(locationRepository.getLastKnownLocation());
        }*/
        locationSubscription = locationRepository.getLocationUpdates()
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
                        locationRepository.cacheLocation(location);
                        updateBusinessAzimuths(location);
                    }
                });
    }

    public boolean deviceIsVerticalWhileTurnedOff() {
        return !turnedOn && groundDeviation > PI_DIVIDED_BY_TWO - PITCH_ACCURACY && groundDeviation < PI_DIVIDED_BY_TWO + PITCH_ACCURACY;
    }

    public boolean deviceIsFlatWhileTurnedOn() {
        return turnedOn && (groundDeviation < PI_DIVIDED_BY_TWO - PITCH_ACCURACY || groundDeviation > PI_DIVIDED_BY_TWO + PITCH_ACCURACY);
    }

    @Override
    public void onDeviceIsFlat() {
        unsubscribeThreeSensors(true, true, true, false);
        view.hideBusiness();
        turnedOn = false;
        view.showToast("Umieść swoje urządzenie pionowo, aby znów zacząć korzystać z trybu rozszerzonej rzeczywistości.");
    }

    public void onDeviceIsVertical() {
        startObservingSensors();
        turnedOn = true;
        view.showToast("Znów korzystasz z trybu rozszerzonej rzeczywistości.");
    }

    /*Based on https://github.com/lycha/augmented-reality-example/blob/master/app/src/main/java/com/lycha/example/augmentedreality/CameraViewActivity.java*/
    public boolean newAzimuthPointsTo(double businessAzimuth, double deviceAzimuth) {
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
    public Boolean isBetween(double minAngle, double maxAngle, double deviceAzimuth) {
        if (minAngle > maxAngle) {
            if (isBetween(0, maxAngle, deviceAzimuth) || isBetween(minAngle, 360, deviceAzimuth))
                return true;
        } else {
            if (deviceAzimuth >= minAngle && deviceAzimuth <= maxAngle)
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

    public float[] lowPassFilter(float[] newValues, float[] oldValues) {
        if (oldValues == null) return newValues;
        for (int i = 0; i < newValues.length; i++) {
            oldValues[i] = oldValues[i] + ALPHA_PARAM * (newValues[i] - oldValues[i]);
        }
        return oldValues;
    }


    @Override
    public void openDetailActivity() {
        view.startDetailActivity(businessRepository.getAugumentedRealityPlaces().get(bestMatchedPlaceIndex));
    }

    public boolean locationsAreTheSame(Location first, Location second) {
        return first.getLatitude() == second.getLatitude() && first.getLongitude() == second.getLongitude();
    }

    private void updateBusinessAzimuths(Location currentLocation) {
        if (!businessRepository.getAugumentedRealityPlaces().isEmpty()) {
            for (int i = 0; i < azimuths.length; i++) {
                azimuths[i] = calculateTeoreticalAzimuth(businessRepository.getAugumentedRealityPlaces().get(i).getCoordinates(), currentLocation);
            }
        }

    }

    /*Based on https://github.com/lycha/augmented-reality-example/blob/master/app/src/main/java/com/lycha/example/augmentedreality/CameraViewActivity.java
    * and Jagielski A, Geodezja 1, str. 201*/
    public double calculateTeoreticalAzimuth(Coordinates coordinates, Location currentLocation) {
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
        businessRepository.safelyUnsubscribe(locationSubscription);

        if (gravity) {
            reactiveSensorManager.unsubscribe(gravitySubscription);
        }
    }


    @Override
    public void setUpReactiveSensorManager(Context context) {
        ReactiveSensors reactiveSensors = new ReactiveSensors(context);
        reactiveSensorManager = new ReactiveSensorManager(reactiveSensors);
    }


    public void managePermissions() {
        RxPermissions rxPermissions = new RxPermissions(view.getViewActivity());
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        view.startObserving();
                    }
                });
    }


    public void setCameraManagerDevice(CameraDevice cameraDevice) {
        cameraManager.setCameraDevice(cameraDevice);
    }

    @Override
    public void onCameraOpened() {
        view.showCameraPreview(cameraManager.getImageDimension(), cameraManager.getmBackgroundHandler(), cameraManager.getCameraDevice());
    }

    public void openCamera(int width, int height, ARActivity activity) {
        cameraManager.openCamera(width, height, activity);
    }

    public void closeCamera() {
        cameraManager.closeCamera();
    }

    public void configureTransform(int width, int height) {
        cameraManager.configureTransform(width, height, (ARActivity) view);
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

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void setAzimuths(double[] azimuths) {
        this.azimuths = azimuths;
    }

    public void setDeviceAzimuth(int deviceAzimuth) {
        this.deviceAzimuth = deviceAzimuth;
    }

    public boolean isPointsTo() {
        return pointsTo;
    }

    public void setPointsTo(boolean pointsTo) {
        this.pointsTo = pointsTo;
    }

    public int getBestMatchedPlaceIndex() {
        return bestMatchedPlaceIndex;
    }

    public ReactiveSensorManager getReactiveSensorManager() {
        return reactiveSensorManager;
    }

    public void setReactiveSensorManager(ReactiveSensorManager reactiveSensorManager) {
        this.reactiveSensorManager = reactiveSensorManager;
    }

    public void setGroundDeviation(float groundDeviation) {
        this.groundDeviation = groundDeviation;
    }
}