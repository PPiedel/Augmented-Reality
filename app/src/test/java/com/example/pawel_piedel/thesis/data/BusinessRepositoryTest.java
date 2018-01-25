package com.example.pawel_piedel.thesis.data;

import android.annotation.SuppressLint;

import com.example.pawel_piedel.thesis.data.business.local.BusinessRepositoryImpl;
import com.example.pawel_piedel.thesis.data.business.local.local.LocalSource;
import com.example.pawel_piedel.thesis.data.business.local.remote.BusinessApiService;
import com.example.pawel_piedel.thesis.data.location.LocationRepository;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;

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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel_Piedel on 01.09.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class BusinessRepositoryTest {

    @Mock
    LocalSource preferenceHelper;

    @Mock
    LocationRepository locationRepository;

    @Mock
    BusinessApiService businessApiService;

    BusinessRepositoryImpl businessRepositoryImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        businessRepositoryImpl = new BusinessRepositoryImpl(businessApiService, preferenceHelper, locationRepository);
    }


    @Test
    public void loadAccessToken() throws Exception {
        //when there is no access token saved in shared preferences
        when(preferenceHelper.getAccessToken()).thenReturn(null);

        AccessToken accessToken = Mockito.mock(AccessToken.class);
        when(businessApiService.loadAccessToken(anyString(), anyString(), anyString())).thenReturn(Observable.just(accessToken));

        //then load access token from Internet
        TestSubscriber<AccessToken> testSubscriber = TestSubscriber.create();
        businessRepositoryImpl.loadAccessToken().subscribe(testSubscriber);

        //assert everything is ok
        testSubscriber.assertReceivedOnNext(Collections.singletonList(accessToken));
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();

    }

    @SuppressLint("MissingPermission")
    @Test
    public void getLocationUpdates() throws Exception {
    }


    @Test
    public void loadBusinessDetails() throws Exception {
        //mock business object and api service behaviour
        Business business = Mockito.mock(Business.class);
        when(businessApiService.getBusinessDetails(anyString())).thenReturn(Observable.just(business));

        TestSubscriber<Business> testSubscriber = TestSubscriber.create();
        businessRepositoryImpl.loadBusinessDetails("example_id").subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(business));
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
    }

    @Test
    public void loadReviews() throws Exception {
        //mock review response and api service behaviour
        ReviewsResponse reviewsResponse = Mockito.mock(ReviewsResponse.class);
        when(businessApiService.getBusinessReviews(anyString(), anyString())).thenReturn(Observable.just(reviewsResponse));

        TestSubscriber<ReviewsResponse> testSubscriber = TestSubscriber.create();
        businessRepositoryImpl.loadReviews("example_id").subscribe(testSubscriber);

        testSubscriber.assertReceivedOnNext(Collections.singletonList(reviewsResponse));
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
    }

    @Test
    public void saveAccessToken() throws Exception {
        AccessToken accessToken = Mockito.mock(AccessToken.class);
        businessRepositoryImpl.saveAccessToken(accessToken);

        verify(preferenceHelper).saveAccessToken(accessToken);

    }


}