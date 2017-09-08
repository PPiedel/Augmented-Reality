package com.example.pawel_piedel.thesis.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.Location;
import com.example.pawel_piedel.thesis.ui.main.BusinessAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Pawel_Piedel on 03.09.2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DetailActivityTest {

    @Mock
    DetailPresenter<DetailContract.View> detailPresenter;

    private DetailActivity detailActivity;
    private ActivityController<DetailActivity> controller;
    private Business intentBusiness;

    @Before
    public void setUp() {
        intentBusiness = setUpBussinessForIntent();

        Intent intent = new Intent();
        intent.putExtra(BusinessAdapter.BUSINESS, intentBusiness);

        controller = Robolectric.buildActivity(DetailActivity.class).withIntent(intent);
        MockitoAnnotations.initMocks(this);

        detailActivity = controller.get();
        detailActivity.setPresenter(detailPresenter);

    }

    @NonNull
    private Business setUpBussinessForIntent() {
        Business business = new Business();
        business.setName("test");
        Location location = new Location();
        location.setAddress1("test");
        location.setAddress2("test");
        location.setZipCode("test");
        location.setCity("test");
        business.setLocation(location);
        business.setPhone("test");
        business.setId("test");
        return business;
    }


    @Test
    public void showBusinessDetails() throws Exception {
    }

    @Test
    public void showReviews() throws Exception {
    }

    @Test
    public void showOldBusiness() throws Exception {
    }

    @Test
    public void onCallButtonClicked() throws Exception {
        detailActivity.onCallButtonClicked();

        verify(detailPresenter).onCallButtonClicked(detailActivity.getNewBusiness());
    }

    @Test
    public void onWebsiteButtonclicked() throws Exception {

    }

    @Test
    public void goToWebsite() throws Exception {
        Intent expectedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://test.pl"));
        Business business = new Business();
        business.setUrl("test.pl");

        detailActivity.goToWebsite(business);

        assertTrue(shadowOf(detailActivity).getNextStartedActivity().filterEquals(expectedIntent));
    }

    @Test
    public void onFavouriteButtonClicked() throws Exception {
    }

    @Test
    public void makeCallShuldCallIntent() throws Exception {
        Intent expectedIntent = new Intent(Intent.ACTION_DIAL);
        Business business = new Business();
        business.setPhone("test");
        expectedIntent.setData(Uri.parse("tel:" + business.getPhone()));

        detailActivity.makeCall(business);

        assertTrue(shadowOf(detailActivity).getNextStartedActivity().filterEquals(expectedIntent));
    }


    @Test
    public void getOldBusinessFromIntent() throws Exception {
        detailActivity.getOldBusinessFromIntent();

        assertEquals(detailActivity.getBusinessId(), intentBusiness.getId());

    }
}