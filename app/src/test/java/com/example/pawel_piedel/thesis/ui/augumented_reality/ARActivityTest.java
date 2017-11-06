package com.example.pawel_piedel.thesis.ui.augumented_reality;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.data.BusinessDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.verify;

/**
 * Created by Pawel_Piedel on 04.11.2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ARActivityTest {

    @Mock
    ARPresenter<ARContract.View> presenter;

    @Mock
    BusinessDataSource businessDataSource;


    private ActivityController<ARActivity> controller;
    private ARActivity activity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = Robolectric.buildActivity(ARActivity.class);
        activity = controller.get();
        activity.setPresenter(presenter);

    }

    @Test
    public void onCreate() throws Exception {
        controller.create();

        verify(presenter).onViewCreated(activity);
    }
}