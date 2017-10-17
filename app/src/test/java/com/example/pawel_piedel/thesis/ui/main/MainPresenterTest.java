package com.example.pawel_piedel.thesis.ui.main;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.ui.main.MainContract;
import com.example.pawel_piedel.thesis.ui.main.MainPresenter;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 30.08.2017.
 */
@RunWith(RobolectricTestRunner.class)
public class MainPresenterTest {

    @Mock
    MainContract.View mainView;

    @Mock
    DataManager dataManager;

    private MainPresenter<MainContract.View> mainPresenter;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mainPresenter = new MainPresenter<>(dataManager);
        mainPresenter.attachView(mainView);

    }


    @Test
    public void checkLocationPermissionsShouldReturnTrue() throws Exception {
        Mockito.when(mainView.hasPermission(ArgumentMatchers.anyString())).thenReturn(true);

        assertTrue(mainPresenter.checkLocationPermissions());
    }

    @Test
    public void checkLocationPermissionShouldReturnFalse() throws  Exception {
        Mockito.when(mainView.hasPermission(ArgumentMatchers.anyString())).thenReturn(false);

        assertFalse(mainPresenter.checkLocationPermissions());
    }

    @Test
    public void requestLocationPermissions() throws Exception {
        mainPresenter.requestLocationPermissions();

        Mockito.verify(mainView).showLocationPermissionsRequest();
    }




}