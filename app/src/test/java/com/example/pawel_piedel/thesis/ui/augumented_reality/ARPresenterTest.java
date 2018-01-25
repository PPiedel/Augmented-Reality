package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.hardware.SensorEvent;
import android.hardware.camera2.CameraDevice;
import android.location.Location;
import android.os.Handler;
import android.util.Size;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.ThesisApplication;
import com.example.pawel_piedel.thesis.data.business.local.BusinessRepository;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.Coordinates;
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
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
    BusinessRepository businessRepository;
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
    Size size;
    @Mock
    CameraManager cameraManager;
    @Mock
    Handler handler;
    @Mock
    CameraDevice cameraDevice;

    private ARPresenter<ARContract.View> presenter;

    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });

        presenter = new ARPresenter<>(businessRepository);
        presenter.attachView(view);
        presenter.setReactiveSensorManager(reactiveSensorManager);
        Assert.assertNotNull(presenter.getView());
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }


    @Test
    public void detachView() throws Exception {
        presenter.detachView();

        assertNull(presenter.getView());
    }

    @Test
    public void attachView() throws Exception {
        presenter.detachView();

        presenter.attachView(view);
        Assert.assertNotNull(presenter.getView());
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
        when(businessRepository.getLastLocation()).thenReturn(location);
        when(businessRepository.getLocationUpdates()).thenReturn(Observable.just(location));

        presenter.observeDeviceLocation();

        verify(businessRepository).getLocationUpdates();
    }

    @Test
    public void openDetailActivity() throws Exception {
        when(businessRepository.getAugumentedRealityPlaces()).thenReturn(Collections.singletonList(business));

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
    public void deviceIsFlatWhileTurnedOnShouldReturnTrue() throws Exception {
        presenter.setTurnedOn(true);
        presenter.setGroundDeviation(0.5f);
        ARPresenter.PITCH_ACCURACY = 0.7;


        assertTrue(presenter.deviceIsFlatWhileTurnedOn());

    }

    @Test
    public void deviceIsFlatWhileTurnedOnShouldReturnFalse() throws Exception {
        presenter.setTurnedOn(false);
        presenter.setGroundDeviation(1.2f);
        ARPresenter.PITCH_ACCURACY = 0.7;


        assertFalse(presenter.deviceIsFlatWhileTurnedOn());

    }

    @Test
    public void deviceIsVerticalWhileWasTurnedOffShouldReturnTrue() throws Exception {
        presenter.setTurnedOn(false);
        presenter.setGroundDeviation(1.2f);
        ARPresenter.PITCH_ACCURACY = 0.7;

        assertTrue(presenter.deviceIsVerticalWhileTurnedOff());
    }

    @Test
    public void deviceIsVerticalWhileWasTurnedOffShouldReturnFalse() throws Exception {
        presenter.setTurnedOn(true);
        presenter.setGroundDeviation(1.2f);
        ARPresenter.PITCH_ACCURACY = 0.7;

        assertFalse(presenter.deviceIsVerticalWhileTurnedOff());
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
    public void onCameraOpened() throws Exception {
        presenter.setCameraManager(cameraManager);
        when(cameraManager.getImageDimension()).thenReturn(size);
        when(cameraManager.getmBackgroundHandler()).thenReturn(handler);
        when(cameraManager.getCameraDevice()).thenReturn(cameraDevice);

        presenter.onCameraOpened();

        verify(view).showCameraPreview(size, handler, cameraDevice);
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
    public void checkPlacesAgainstNewAzimuthShouldReturnTrueAndCorrectIndex() throws Exception {
        double[] azimuths = new double[]{10, 20, 30};
        ARPresenter.AZIMUTH_ACCURACY = 5;
        presenter.setDeviceAzimuth(10);
        presenter.setPointsTo(false);
        presenter.setAzimuths(azimuths);

        presenter.checkPlacesAzimuthsAgainstNewAzimuth();

        assertTrue(presenter.isPointsTo());
        assertEquals(0, presenter.getBestMatchedPlaceIndex());


    }

    @Test
    public void checkPlacesAgainstNewAzimuthShouldReturnTrueAndCorrectIndex2() throws Exception {
        double[] azimuths = new double[]{0, 15, 30};
        ARPresenter.AZIMUTH_ACCURACY = 5;
        presenter.setDeviceAzimuth(10);
        presenter.setPointsTo(false);
        presenter.setAzimuths(azimuths);

        presenter.checkPlacesAzimuthsAgainstNewAzimuth();

        assertTrue(presenter.isPointsTo());
        assertEquals(1, presenter.getBestMatchedPlaceIndex());


    }

    @Test
    public void checkplacesagainstnewazimuthShouldReturnFalse() throws Exception {
        double[] azimuths = new double[]{0, 35, 30};
        ARPresenter.AZIMUTH_ACCURACY = 5;
        presenter.setDeviceAzimuth(15);
        presenter.setPointsTo(false);
        presenter.setAzimuths(azimuths);

        presenter.checkPlacesAzimuthsAgainstNewAzimuth();

        assertFalse(presenter.isPointsTo());

    }

    @Test
    public void lowPass() throws Exception {
        ARPresenter.ALPHA_PARAM = 0.5f;
        float[] values = presenter.lowPassFilter(new float[]{0, 0, 0}, new float[]{30, 30, 30});

        for (float v : values) {
            assertEquals(15f, v);
        }
    }

    @Test
    public void locationsAreTheSameShouldReturnTrue() throws Exception {
        Location first = new Location("first");
        first.setLatitude(50);
        first.setLongitude(45);
        Location second = new Location("second");
        second.setLatitude(50);
        second.setLongitude(45);

        assertTrue(presenter.locationsAreTheSame(first, second));
    }

    @Test
    public void locationsAreTheSameShouldReturnFalse() throws Exception {
        Location first = new Location("first");
        first.setLatitude(50);
        first.setLongitude(45);
        Location second = new Location("second");
        second.setLatitude(50);
        second.setLatitude(90);

        assertFalse(presenter.locationsAreTheSame(first, second));
    }

    @Test
    public void calculateTheoreticalAzimuthShouldReturn45() throws Exception {
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(50);
        coordinates.setLongitude(50);

        Location location = new Location("test");
        location.setLatitude(30);
        location.setLongitude(30);
        double azimuth = presenter.calculateTeoreticalAzimuth(coordinates, location);
        assertEquals((double) 45, azimuth);

    }

    @Test
    public void calculateTheoreticalAzimuthShouldReturn90() throws Exception {
        Coordinates coordinates = new Coordinates();
        coordinates.setLongitude(0);
        coordinates.setLatitude(0);


        Location location = new Location("test");
        location.setLongitude(90);
        location.setLatitude(0);

        double azimuth = presenter.calculateTeoreticalAzimuth(coordinates, location);
        assertEquals((double) 90, azimuth);

    }

    @Test
    public void calculateTheoreticalAzimuthShouldReturn0() throws Exception {
        Coordinates coordinates = new Coordinates();
        coordinates.setLongitude(0);
        coordinates.setLatitude(0);


        Location location = new Location("test");
        location.setLongitude(0);
        location.setLatitude(90);

        double azimuth = presenter.calculateTeoreticalAzimuth(coordinates, location);
        assertEquals((double) 0, azimuth);

    }


}