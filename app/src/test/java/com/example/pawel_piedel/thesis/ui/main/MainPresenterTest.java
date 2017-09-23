package com.example.pawel_piedel.thesis.ui.main;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.ui.main.MainContract;
import com.example.pawel_piedel.thesis.ui.main.MainPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pawel_Piedel on 30.08.2017.
 */
@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void onFabClick() throws Exception {
        mainPresenter.onFabClick();

        Mockito.verify(mainView).startArActivity();
    }

    @Test
    public void onPermissionResult() throws Exception {
    }



}