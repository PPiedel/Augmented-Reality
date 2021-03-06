package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.location.Location;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.data.business.local.BusinessRepositoryImpl;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 02.09.2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CafesPresenterTest {

    CafesPresenter<CafesContract.View> presenter;

    @Mock
    CafesContract.View view;
    @Mock
    RxPermissions rxPermissions;
    @Mock
    BusinessRepositoryImpl businessRepositoryImpl;
    @Mock
    AccessToken accessToken;
    @Mock
    Location location;


    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        presenter = new CafesPresenter<>(businessRepositoryImpl);
        presenter.attachView(view);
    }

    @Test
    public void requestPermissions() throws Exception {
        view.setRxPermissions(rxPermissions);
        when(view.getRxPermissions()).thenReturn(rxPermissions);
        when(rxPermissions.request(anyString(), anyString())).thenReturn(Observable.just(true));


        TestSubscriber<Boolean> testSubscriber = TestSubscriber.create();

        presenter.requestPermissions().subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(true));
        testSubscriber.onCompleted();
        testSubscriber.assertNoErrors();

    }

}