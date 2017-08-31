package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.hardware.SensorEvent;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.augumented_reality.AzimuthManager;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Observable;

import rx.Completable;
import rx.observers.TestSubscriber;

import static com.bumptech.glide.Glide.with;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 30.08.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ARPresenterTest {

    @Mock
    ARContract.View view;

    @Mock
    AzimuthManager azimuthManager;

    @Mock
    DataManager dataManager;

    private ARPresenter<ARContract.View> presenter;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        presenter = new ARPresenter<>(dataManager);
    }

    @Test
    public void attachView() throws Exception {
        presenter.attachView(view);
        assertNotNull(presenter.getView());
    }

    @Test
    public void startObservingAzimuthShouldProccessOk() throws Exception {
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
    public void startObservingLocation() throws Exception {
    }

    @Test
    public void openDetailActivity() throws Exception {
    }

    @Test
    public void unsubscribeAll() throws Exception {
    }

    @Test
    public void setReactiveSensors() throws Exception {
    }

    @Test
    public void managePermissions() throws Exception {
    }

    @Test
    public void onPermissionResult() throws Exception {
    }

    @Test
    public void openCamera() throws Exception {
    }

    @Test
    public void closeCamera() throws Exception {
    }

    @Test
    public void createCameraPreview() throws Exception {
    }

    @Test
    public void startBackgroundThread() throws Exception {
    }

    @Test
    public void stopBackgroundThread() throws Exception {
    }

    @Test
    public void configureTransform() throws Exception {
    }

}