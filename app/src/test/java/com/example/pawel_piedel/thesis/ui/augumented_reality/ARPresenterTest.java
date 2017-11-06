package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.hardware.SensorEvent;
import android.location.Location;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.ThesisApplication;
import com.example.pawel_piedel.thesis.data.BusinessDataSource;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.annotation.Config;

import java.util.Collections;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaTestRunner;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by Pawel_Piedel on 30.08.2017.
 */
@RunWith(RxJavaTestRunner.class)
@Config(constants = BuildConfig.class, application = ThesisApplication.class)
public class ARPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    ARContract.View view;
    @Mock
    BusinessDataSource businessDataSource;
    @Mock
    ReactiveSensorManager reactiveSensorManager;
    @Mock
    SensorEvent sensorEvent;
    @Mock
    Location location;
    @Mock
    Business business;
    @Mock
    Subscription subscription;

    @Mock
    CameraManager cameraManager;

    private ARPresenter<ARContract.View> presenter;

    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });

        presenter = new ARPresenter<>(businessDataSource);
        presenter.attachView(view);
        presenter.setReactiveSensorManager(reactiveSensorManager);
        Assert.assertNotNull(presenter.getView());
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }


    @Test
    public void observeDeviceAzimuthShouldCallToast() throws Exception {
        when(reactiveSensorManager.hasSensor(anyInt())).thenReturn(false);

        presenter.observeDeviceAzimuth();

        verify(view).showToast(anyString());

    }

    @Test
    public void observeDeviceAzimuth() throws Exception {
        when(reactiveSensorManager.hasSensor(anyInt())).thenReturn(true);
        when(reactiveSensorManager.getReactiveSensorEvents(anyInt(), anyInt())).thenReturn(Observable.just(new ReactiveSensorEvent(sensorEvent)));

        presenter.observeDeviceAzimuth();

        verify(reactiveSensorManager).getReactiveSensorEvents(anyInt(), anyInt());


    }

    @Test
    public void observeDeviceAzimuthAccuracy() throws Exception {
        when(reactiveSensorManager.hasSensor(anyInt())).thenReturn(true);
        when(reactiveSensorManager.getReactiveSensorAccuracy(anyInt())).thenReturn(Observable.just(new ReactiveSensorEvent(sensorEvent)));

        presenter.observeDeviceAzimuthAccuracy();

        verify(reactiveSensorManager).getReactiveSensorAccuracy(anyInt());
    }

    @Test
    public void observeGravitySensor() throws Exception {
        when(reactiveSensorManager.hasSensor(anyInt())).thenReturn(true);
        when(reactiveSensorManager.getReactiveSensorEvents(anyInt(), anyInt())).thenReturn(Observable.just(new ReactiveSensorEvent(sensorEvent)));

        presenter.observeGravitySensor();

        verify(reactiveSensorManager).getReactiveSensorEvents(anyInt(), anyInt());
    }

    @Test
    public void observeDeviceLocation() throws Exception {
        when(businessDataSource.getLastLocation()).thenReturn(location);
        when(businessDataSource.getLocationUpdates()).thenReturn(Observable.just(location));

        presenter.observeDeviceLocation();

        verify(businessDataSource).getLocationUpdates();
    }

    @Test
    public void openDetailActivity() throws Exception {
        when(businessDataSource.getAugumentedRealityPlaces()).thenReturn(Collections.singletonList(business));

        presenter.openDetailActivity();

        verify(view).startDetailActivity(business);
    }


    @Test
    public void onDeviceIsFlat() {
        presenter.onDeviceIsFlat();


        verify(view).hideBusiness();
        verify(view).showToast(anyString());
        assert !presenter.isTurnedOn();
    }

    @Test
    public void closeCamera() throws Exception {
        presenter.setCameraManager(cameraManager);
        presenter.closeCamera();

        verify(cameraManager).closeCamera();
    }

    @Test
    public void openCamera() throws Exception {
        presenter.setCameraManager(cameraManager);

        int test = 0;
        ARActivity testActivity = new ARActivity();
        presenter.openCamera(test, test, testActivity);

        verify(cameraManager).openCamera(test, test, testActivity);
    }

    @Test
    public void newAzimuthPointsToReturnsTrue() {
        ARPresenter.AZIMUTH_ACCURACY = 10;
        double deviceAzimuth = 15;
        double businessAzimuth = 20;

        assertTrue(presenter.newAzimuthPointsTo(businessAzimuth, deviceAzimuth));

    }

    @Test
    public void newAzimuthPointsToReturnsTrue2() {
        ARPresenter.AZIMUTH_ACCURACY = 10;
        double deviceAzimuth = 8;
        double businessAzimuth = 5;

        assertTrue(presenter.newAzimuthPointsTo(businessAzimuth, deviceAzimuth));

    }

    @Test
    public void newAzimuthPointsToReturnsTrue3() {
        ARPresenter.AZIMUTH_ACCURACY = 10;
        double deviceAzimuth = 8;
        double businessAzimuth = 8;

        assertTrue(presenter.newAzimuthPointsTo(businessAzimuth, deviceAzimuth));

    }

    @Test
    public void newAzimuthPointsToReturnsTrue4() {
        ARPresenter.AZIMUTH_ACCURACY = 8;
        double deviceAzimuth = 0;
        double businessAzimuth = 0;

        assertTrue(presenter.newAzimuthPointsTo(businessAzimuth, deviceAzimuth));

    }

    @Test
    public void newAzimuthPointsToReturnsTrue6() {
        ARPresenter.AZIMUTH_ACCURACY = 8;
        double deviceAzimuth = 360;
        double businessAzimuth = 0;

        assertTrue(presenter.newAzimuthPointsTo(businessAzimuth, deviceAzimuth));

    }

    @Test
    public void newAzimuthPointsToReturnsTrue5() {
        ARPresenter.AZIMUTH_ACCURACY = 5;
        double deviceAzimuth = 360;
        double businessAzimuth = 360;

        assertTrue(presenter.newAzimuthPointsTo(businessAzimuth, deviceAzimuth));

    }

    @Test
    public void newAzimuthPointsToReturnsTrue7() {
        ARPresenter.AZIMUTH_ACCURACY = 5;
        double deviceAzimuth = 359;
        double businessAzimuth = 3;

        assertTrue(presenter.newAzimuthPointsTo(businessAzimuth, deviceAzimuth));

    }

    @Test
    public void newAzimuthPointsToReturnFalse() {
        ARPresenter.AZIMUTH_ACCURACY = 10;
        double deviceAzimuth = 9;
        double businessAzimuth = 20;

        assertFalse(presenter.newAzimuthPointsTo(businessAzimuth, deviceAzimuth));

    }

    @Test
    public void isBetweenShouldReturnTrue() throws Exception {
        assertTrue(presenter.isBetween(20, 30, 25));
    }

    @Test
    public void isBetweenShouldReturnTrue2() throws Exception {
        assertTrue(presenter.isBetween(358, 10, 3));
    }

    @Test
    public void isBetweenShouldReturnTrue3() throws Exception {
        assertTrue(presenter.isBetween(0, 10, 9));
    }

    @Test
    public void isBetweenShouldReturnTrue4() throws Exception {
        assertTrue(presenter.isBetween(350, 10, 358));
    }


    @Test
    public void checkPlacesAgainstNewAzimuth() throws Exception {
        double[] azimuths = new double[]{10, 20, 30};


    }


}