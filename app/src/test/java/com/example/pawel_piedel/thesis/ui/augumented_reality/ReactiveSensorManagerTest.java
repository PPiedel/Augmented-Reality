package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 31.08.2017.
 */
public class ReactiveSensorManagerTest {

    @Mock
    ReactiveSensorManager reactiveSensorManager;

    @Mock
    ReactiveSensorEvent reactiveSensorEvent;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getReactiveSensorEventsShouldReturnObservable() throws Exception {
        SensorEvent sensorEvent = Mockito.mock(SensorEvent.class);
        ReactiveSensorEvent reactiveSensorEvent = new ReactiveSensorEvent(sensorEvent);
        when(reactiveSensorManager.getReactiveSensorEvents(Sensor.TYPE_ROTATION_VECTOR, SensorManager.SENSOR_DELAY_NORMAL)).thenReturn(rx.Observable.just(reactiveSensorEvent));

        TestSubscriber<ReactiveSensorEvent> subscriber = new TestSubscriber<>();
        reactiveSensorManager.getReactiveSensorEvents(Sensor.TYPE_ROTATION_VECTOR, SensorManager.SENSOR_DELAY_NORMAL).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertReceivedOnNext(Collections.singletonList(reactiveSensorEvent));
        subscriber.assertCompleted();
    }

    @Test
    public void getReactiveSensorAccuracyShouldReturnObservable() throws Exception {
        int test = 0;
        when(reactiveSensorManager.getReactiveSensorAccuracy(test)).thenReturn(Observable.just(reactiveSensorEvent));


        TestSubscriber<ReactiveSensorEvent> subscriber = new TestSubscriber<>();
        reactiveSensorManager.getReactiveSensorAccuracy(test).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertReceivedOnNext(Collections.singletonList(reactiveSensorEvent));
        subscriber.onCompleted();

    }

    @Test
    public void unsubscribe() throws Exception {
        SensorEvent sensorEvent = Mockito.mock(SensorEvent.class);
        ReactiveSensorEvent reactiveSensorEvent = new ReactiveSensorEvent(sensorEvent);
        when(reactiveSensorManager.getReactiveSensorEvents(Sensor.TYPE_ROTATION_VECTOR, SensorManager.SENSOR_DELAY_NORMAL)).thenReturn(rx.Observable.just(reactiveSensorEvent));

        Subscription subscription = reactiveSensorManager.getReactiveSensorEvents(Sensor.TYPE_ROTATION_VECTOR, SensorManager.SENSOR_DELAY_NORMAL).subscribe();

        reactiveSensorManager.unsubscribe(subscription);

        assertNotNull(subscription);
        assertTrue(subscription.isUnsubscribed());


    }

}