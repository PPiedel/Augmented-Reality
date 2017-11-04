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
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaTestRunner;
import rx.schedulers.Schedulers;

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
        Assert.assertNotNull(presenter.getView());
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }


    @Test
    public void observeDeviceAzimuthShouldCallToast() throws Exception {
        presenter.setReactiveSensorManager(reactiveSensorManager);
        when(reactiveSensorManager.hasSensor(anyInt())).thenReturn(false);

        presenter.observeDeviceAzimuth();

        verify(view).showToast(anyString());

    }

    @Test
    public void observeDeviceAzimuth() throws Exception {
        presenter.setReactiveSensorManager(reactiveSensorManager);
        when(reactiveSensorManager.hasSensor(anyInt())).thenReturn(true);
        when(reactiveSensorManager.getReactiveSensorEvents(anyInt(), anyInt())).thenReturn(Observable.just(new ReactiveSensorEvent(sensorEvent)));

        presenter.observeDeviceAzimuth();

        verify(reactiveSensorManager).getReactiveSensorEvents(anyInt(), anyInt());


    }

    @Test
    public void observeDeviceAzimuthAccuracy() throws Exception {
        presenter.setReactiveSensorManager(reactiveSensorManager);
        when(reactiveSensorManager.hasSensor(anyInt())).thenReturn(true);
        when(reactiveSensorManager.getReactiveSensorAccuracy(anyInt())).thenReturn(Observable.just(new ReactiveSensorEvent(sensorEvent)));

        presenter.observeDeviceAzimuthAccuracy();

        verify(reactiveSensorManager).getReactiveSensorAccuracy(anyInt());
    }

    @Test
    public void observeGravitySensor() throws Exception {
        presenter.setReactiveSensorManager(reactiveSensorManager);
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


}