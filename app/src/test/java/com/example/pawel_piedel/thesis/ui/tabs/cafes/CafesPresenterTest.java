package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.location.Location;
import android.util.Pair;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.example.pawel_piedel.thesis.data.remote.ServiceFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

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
    DataManager dataManager;

    @Before
    public void setUpSchedulers() throws Exception {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
        presenter = new CafesPresenter<>(dataManager);
        presenter.attachView(view);
    }


    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }


    @Test
    public void load() throws Exception {
        AccessToken accessToken = Mockito.mock(AccessToken.class);
        Location location = Mockito.mock(Location.class);
        Pair<AccessToken,Location> pair = new Pair<>(accessToken,location);
        when(dataManager.loadAccessTokenLocationPair()).thenReturn(rx.Observable.just(pair));

        presenter.load();

        verify(view).showProgressDialog();


    }

    @Test
    public void loadCafes() throws Exception {
        SearchResponse searchResponse = Mockito.mock(SearchResponse.class);
        when(dataManager.loadBusinesses(anyString(),anyString())).thenReturn(rx.Observable.just(searchResponse));

        TestSubscriber<SearchResponse> testSubscriber = TestSubscriber.create();
        dataManager.loadBusinesses("example_term","example_category").subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(searchResponse));
        testSubscriber.onCompleted();
        testSubscriber.assertNoErrors();


    }

}