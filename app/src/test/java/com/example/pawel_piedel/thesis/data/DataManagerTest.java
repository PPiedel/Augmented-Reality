package com.example.pawel_piedel.thesis.data;

import android.annotation.SuppressLint;
import android.location.Location;
import android.test.mock.MockContext;

import com.example.pawel_piedel.thesis.data.local.SharedPreferencesManager;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;
import com.example.pawel_piedel.thesis.data.remote.ApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 01.09.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock
    SharedPreferencesManager preferenceHelper;

    @Mock
    ApiService apiService;

    DataManager dataManager;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        dataManager = new DataManager(new MockContext(),apiService, preferenceHelper);
    }


    @Test
    public void loadAccessToken() throws Exception {
        //when there is no access token saved in shared preferences
        when(preferenceHelper.getAccessToken()).thenReturn(null);

        AccessToken accessToken = Mockito.mock(AccessToken.class);
        when(apiService.getAccessToken(anyString(),anyString(),anyString())).thenReturn(Observable.just(accessToken));

        //then load access token from Internet
        TestSubscriber<AccessToken> testSubscriber = TestSubscriber.create();
        dataManager.loadAccessToken().subscribe(testSubscriber);

        //assert everything is ok
        testSubscriber.assertReceivedOnNext(Collections.singletonList(accessToken));
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();

    }

    @Test
    public void getLastKnownLocationFromDatamanagerField() throws Exception {
        Location lastlocation = Mockito.mock(Location.class);
        dataManager.setLastLocation(lastlocation);

        TestSubscriber<Location> locationTestSubscriber = TestSubscriber.create();

        dataManager.getLastKnownLocation().subscribe(locationTestSubscriber);

        locationTestSubscriber.assertNoErrors();
        locationTestSubscriber.assertCompleted();
        locationTestSubscriber.assertReceivedOnNext(Collections.singletonList(lastlocation));


    }

    @SuppressLint("MissingPermission")
    @Test
    public void getLocationUpdates() throws Exception {
    }


    @Test
    public void loadBusinessDetails() throws Exception {
        //mock business object and api service behaviour
        Business business = Mockito.mock(Business.class);
        when(apiService.getBusinessDetails(anyString())).thenReturn(Observable.just(business));

        TestSubscriber<Business> testSubscriber = TestSubscriber.create();
        dataManager.loadBusinessDetails("example_id").subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(business));
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
    }

    @Test
    public void loadReviews() throws Exception {
        //mock review response and api service behaviour
        ReviewsResponse reviewsResponse = Mockito.mock(ReviewsResponse.class);
        when(apiService.getBusinessReviews(anyString(),anyString())).thenReturn(Observable.just(reviewsResponse));

        TestSubscriber<ReviewsResponse> testSubscriber = TestSubscriber.create();
        dataManager.loadReviews("example_id").subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(reviewsResponse));
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
    }

    @Test
    public void saveAccessToken() throws Exception {
        AccessToken accessToken = Mockito.mock(AccessToken.class);
        dataManager.saveAccessToken(accessToken);

        verify(preferenceHelper).saveAccessToken(accessToken);

    }


}