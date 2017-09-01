package com.example.pawel_piedel.thesis.ui.main;

import com.example.pawel_piedel.thesis.BuildConfig;

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
import static org.mockito.Mockito.verify;

/**
 * Created by Pawel_Piedel on 01.09.2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    private final int TEST_CODE = 1234;
    private final String[] TEST_PERMISSIONS = {"2134","test"};
    private final int[] TEST_GRANT_RESULTS = {1,2,3};



    @Mock
    MainPresenter<MainContract.View> mainPresenter;

    @InjectMocks
    private MainActivity activity;

    private ActivityController<MainActivity> controller;


    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(MainActivity.class);

        MockitoAnnotations.initMocks(this);

        activity = controller.get();

        activity.setmPresenter(mainPresenter);

    }

    @Test
    public void onCreate() throws Exception {
        controller.create().start().resume();
        verify(mainPresenter).attachView(activity);
    }

    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void onDestroy() throws Exception {
    }

    @Test
    public void onRequestPermissionsResult() throws Exception {
        activity.onRequestPermissionsResult(TEST_CODE, TEST_PERMISSIONS,TEST_GRANT_RESULTS);
        verify(mainPresenter).onPermissionResult(TEST_CODE,TEST_GRANT_RESULTS);
    }


    @Test
    public void getViewActivity() throws Exception {
    }

    @Test
    public void startArActivity() throws Exception {
    }

    @Test
    public void fabOnClick() throws Exception {
        activity.fabOnClick();

        verify(mainPresenter).onFabClick();
    }

}