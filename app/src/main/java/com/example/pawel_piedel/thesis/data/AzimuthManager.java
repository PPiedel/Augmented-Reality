package com.example.pawel_piedel.thesis.data;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by Pawel_Piedel on 25.07.2017.
 */

public class AzimuthManager {
    private final ReactiveSensors reactiveSensors;
    private final int sensorType;


    public AzimuthManager(ReactiveSensors reactiveSensors, int sensorType) {
        this.reactiveSensors = reactiveSensors;
        this.sensorType = sensorType;
    }

    public Observable<ReactiveSensorEvent> getReactiveSensorEvents() {
        return reactiveSensors.observeSensor(sensorType)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorFilter.filterSensorChanged());
    }

    public Observable<ReactiveSensorEvent> getReactiveSensorAccuracy(){
        return reactiveSensors.observeSensor(sensorType)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorFilter.filterAccuracyChanged());
    }

    public void unsubscribe(Subscription subscription) {
        subscription.unsubscribe();
    }
}
