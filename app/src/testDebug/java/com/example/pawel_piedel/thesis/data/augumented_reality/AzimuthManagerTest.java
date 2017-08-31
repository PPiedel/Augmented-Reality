package com.example.pawel_piedel.thesis.data.augumented_reality;

import android.hardware.SensorEvent;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 31.08.2017.
 */
public class AzimuthManagerTest {

    @Mock
    AzimuthManager azimuthManager;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getReactiveSensorEventsShouldReturnValue() throws Exception {
        SensorEvent sensorEvent = Mockito.mock(SensorEvent.class);
        ReactiveSensorEvent reactiveSensorEvent = new ReactiveSensorEvent(sensorEvent);
        when(azimuthManager.getReactiveSensorEvents()).thenReturn(rx.Observable.just(reactiveSensorEvent));

        TestSubscriber<ReactiveSensorEvent> subscriber = new TestSubscriber<>();
        azimuthManager.getReactiveSensorEvents().subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertReceivedOnNext(Collections.singletonList(reactiveSensorEvent));
        subscriber.assertCompleted();
    }

    @Test
    public void unsubscribe() throws Exception {
        SensorEvent sensorEvent = Mockito.mock(SensorEvent.class);
        ReactiveSensorEvent reactiveSensorEvent = new ReactiveSensorEvent(sensorEvent);
        when(azimuthManager.getReactiveSensorEvents()).thenReturn(rx.Observable.just(reactiveSensorEvent));

        Subscription subscription = azimuthManager.getReactiveSensorEvents().subscribe();

        azimuthManager.unsubscribe(subscription);

        assertNotNull(subscription);
        assertTrue(subscription.isUnsubscribed());


    }

}