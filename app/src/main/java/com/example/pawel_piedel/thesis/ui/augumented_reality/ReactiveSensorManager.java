package com.example.pawel_piedel.thesis.ui.augumented_reality;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by Pawel_Piedel on 25.07.2017.
 */

public class ReactiveSensorManager {
    private final ReactiveSensors reactiveSensors;


    public ReactiveSensorManager(ReactiveSensors reactiveSensors) {
        this.reactiveSensors = reactiveSensors;
    }

    public Observable<ReactiveSensorEvent> getReactiveSensorEvents(int sensorType, int delay) {
        return reactiveSensors.observeSensor(sensorType,delay)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorFilter.filterSensorChanged());
    }

    public Observable<ReactiveSensorEvent> getReactiveSensorAccuracy(int sensorType){
        return reactiveSensors.observeSensor(sensorType)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorFilter.filterAccuracyChanged());
    }

    public void unsubscribe(Subscription subscription) {
        subscription.unsubscribe();
    }
}
