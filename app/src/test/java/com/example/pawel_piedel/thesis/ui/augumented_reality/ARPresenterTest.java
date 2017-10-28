package com.example.pawel_piedel.thesis.ui.augumented_reality;

import com.example.pawel_piedel.thesis.data.DataManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;


/**
 * Created by Pawel_Piedel on 30.08.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ARPresenterTest {

    @Mock
    ARContract.View view;

    @Mock
    DataManager dataManager;

    @Mock
    ReactiveSensorManager reactiveSensorManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private ARPresenter<ARContract.View> presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new ARPresenter<>(dataManager);

    }

    @Test
    public void attachView() throws Exception {
        presenter.attachView(view);
        Assert.assertNotNull(presenter.getView());
    }

    @Test
    public void startObservingAzimuthShouldCallViewToast() throws Exception {
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