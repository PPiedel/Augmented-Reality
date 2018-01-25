package com.example.pawel_piedel.thesis.ui.tabs.restaurants;

import android.location.Location;

import com.example.pawel_piedel.thesis.data.business.local.BusinessRepositoryImpl;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 07.11.2017.
 */
public class RestaurantsPresenterTest {

    RestaurantsPresenter<RestaurantsContract.View> presenter;

    @Mock
    RestaurantsContract.View view;
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
        presenter = new RestaurantsPresenter<>(businessRepositoryImpl);
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