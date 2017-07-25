package com.example.pawel_piedel.thesis.ui.augumented_reality;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Pawel_Piedel on 25.07.2017.
 */

public class AzimuthManager {
    private final static String LOG_TAG = AzimuthManager.class.getSimpleName();
    private ReactiveSensors reactiveSensors;
    private int sensorType;


    public AzimuthManager(ReactiveSensors reactiveSensors, int sensorType) {
        this.reactiveSensors = reactiveSensors;
        this.sensorType = sensorType;
    }

    public Observable<ReactiveSensorEvent> getReactiveSensorEvents() {
        return reactiveSensors.observeSensor(sensorType)
                .subscribeOn(Schedulers.computation())
                .filter(ReactiveSensorFilter.filterSensorChanged());
    }

    public void safelyUnsubscribe(Subscription subscription) {
        if (!reactiveSensors.hasSensor(sensorType)) {
            return;
        }

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
