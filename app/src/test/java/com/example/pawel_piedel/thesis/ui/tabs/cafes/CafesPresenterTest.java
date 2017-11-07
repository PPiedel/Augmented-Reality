package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.location.Location;
import android.util.Pair;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.data.BusinessRepository;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;

import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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
    BusinessRepository businessRepository;


    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        presenter = new CafesPresenter<>(businessRepository);
        presenter.attachView(view);
    }

    @Test
    public void load() throws Exception {
        AccessToken accessToken = Mockito.mock(AccessToken.class);
        Location location = Mockito.mock(Location.class);
        Pair<AccessToken,Location> pair = new Pair<>(accessToken,location);
        when(businessRepository.loadAccessTokenLocationPair()).thenReturn(rx.Observable.just(pair));
        when(rxPermissions.isGranted(anyString())).thenReturn(true);

        presenter.load();

        verify(view).showProgressDialog();


    }

    @Test
    public void loadCafes() throws Exception {
        SearchResponse searchResponse = Mockito.mock(SearchResponse.class);
        when(businessRepository.loadBusinesses(anyString(), anyString())).thenReturn(rx.Observable.just(searchResponse));

        TestSubscriber<SearchResponse> testSubscriber = TestSubscriber.create();
        businessRepository.loadBusinesses("example_term", "example_category").subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(searchResponse));
        testSubscriber.onCompleted();
        testSubscriber.assertNoErrors();


    }

}