package com.example.pawel_piedel.thesis.ui.augumented_reality;

import com.example.pawel_piedel.thesis.BuildConfig;

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
    ARPresenter<ARContract.View> arPresenter;

    private ActivityController<ARActivity> controller;
    private ARActivity activity;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(ARActivity.class);

        MockitoAnnotations.initMocks(this);

        activity = controller.get();
        activity.setPresenter(arPresenter);

    }

    @Test
    public void onCreate() throws Exception {
        controller.create();

        verify(arPresenter).onViewCreated(activity);
    }

    @Test
    public void onResume() throws Exception {
    }

    @Test
    public void startObserving() throws Exception {
    }

    @Test
    public void onPause() throws Exception {
    }

    @Test
    public void onDestroy() throws Exception {
    }

    @Test
    public void showBusinessOnScreen() throws Exception {
    }

    @Test
    public void hideBusiness() throws Exception {
    }

    @Test
    public void getViewActivity() throws Exception {
    }

    @Test
    public void showCameraPreview() throws Exception {
    }

    @Test
    public void setTransform() throws Exception {
    }

    @Test
    public void showToast() throws Exception {
    }

    @Test
    public void setAspectRatio() throws Exception {
    }

    @Test
    public void startDetailActivity() throws Exception {
    }

    @Test
    public void businessViewOnClick() throws Exception {
    }

}