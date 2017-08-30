package com.example.pawel_piedel.thesis.ui.main;

import com.example.pawel_piedel.thesis.data.DataManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 30.08.2017.
 */
public class MainPresenterTest {

    @Mock
    private MainContract.View mainView;

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
    public void manageLocationPermissionsShouldCallRequestPermissions() throws Exception {
        when(mainView.hasPermission(anyString())).thenReturn(false);

        mainPresenter.manageLocationPermissions();

        verify(mainView).showLocationPermissionsRequest();
    }

    @Test
    public void manageLocationPermissionsShouldDoNothing() throws Exception {
        when(mainView.hasPermission(anyString())).thenReturn(true);

        mainPresenter.manageLocationPermissions();

        verify(mainView,never()).showLocationPermissionsRequest();
    }

    @Test
    public void checkLocationPermissionsShouldReturnTrue() throws Exception {
        when(mainView.hasPermission(anyString())).thenReturn(true);

        assertTrue(mainPresenter.checkLocationPermissions());
    }

    @Test
    public void checkLocationPermissionShouldReturnFalse() throws  Exception {
        when(mainView.hasPermission(anyString())).thenReturn(false);

        assertFalse(mainPresenter.checkLocationPermissions());
    }

    @Test
    public void requestLocationPermissions() throws Exception {
        mainPresenter.requestLocationPermissions();

        verify(mainView).showLocationPermissionsRequest();
    }

    @Test
    public void onFabClick() throws Exception {
        mainPresenter.onFabClick();

        verify(mainView).startArActivity();
    }

    @Test
    public void onPermissionResult() throws Exception {
    }



}