package com.example.pawel_piedel.thesis.ui.base;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.ui.main.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 02.09.2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BaseActivityTest {

    BaseActivity baseActivity;


    @Before
    public void setUp() {
        baseActivity = Robolectric.setupActivity(BaseActivity.class);


        assertNotNull(baseActivity);

    }

    @Test
    public void showLocationPermissionsRequest() throws Exception {

    }

    @Test
    public void showCameraPermissionRequest() throws Exception {

    }


    @Test
    public void requestRequiredPermissions() throws Exception {
    }

    @Test
    public void showProgressDialog() throws Exception {
    }

    @Test
    public void hideProgressDialog() throws Exception {
    }

    @Test
    public void hasPermission() throws Exception {
    }

}