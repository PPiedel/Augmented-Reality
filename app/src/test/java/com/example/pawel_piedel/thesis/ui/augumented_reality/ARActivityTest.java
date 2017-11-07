package com.example.pawel_piedel.thesis.ui.augumented_reality;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.data.model.Business;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.mockito.ArgumentMatchers.anyBoolean;
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
    Business business;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onResume() throws Exception {
        ActivityController<ARActivity> controller = Robolectric.buildActivity(ARActivity.class).create().start();
        ARActivity activity = controller.get();
        activity.setPresenter(presenter);
        controller.resume();


        verify(presenter).managePermissions();
    }

    @Test
    public void onPause() throws Exception {
        ActivityController<ARActivity> controller = Robolectric.buildActivity(ARActivity.class).create().start();
        ARActivity activity = controller.get();
        activity.setPresenter(presenter);
        controller.pause();


        verify(presenter).closeCamera();
        verify(presenter).stopBackgroundThread();
        verify(presenter).unsubscribeThreeSensors(anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean());
    }

    @Test
    public void onDestroy() throws Exception {
        ActivityController<ARActivity> controller = Robolectric.buildActivity(ARActivity.class).create().start();
        ARActivity activity = controller.get();
        activity.setPresenter(presenter);
        controller.destroy();


        verify(presenter).detachView();
    }

    @Test
    public void startObserving() throws Exception {
        ActivityController<ARActivity> controller = Robolectric.buildActivity(ARActivity.class).create().start();
        ARActivity activity = controller.get();
        activity.setPresenter(presenter);
        activity.startObserving();


        verify(presenter).startCameraBackgroundThread();
        verify(presenter).observeGravitySensor();
        verify(presenter).startObservingSensors();
    }

}