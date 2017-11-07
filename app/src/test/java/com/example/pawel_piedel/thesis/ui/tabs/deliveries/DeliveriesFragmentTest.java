package com.example.pawel_piedel.thesis.ui.tabs.deliveries;

import com.example.pawel_piedel.thesis.BuildConfig;
import com.example.pawel_piedel.thesis.data.model.Business;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by Pawel_Piedel on 07.11.2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DeliveriesFragmentTest {

    @Mock
    DeliveriesPresenter<DeliveriesContract.View> presenter;

    DeliveriesFragment fragment;

    @Mock
    Business business;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onCreateView() throws Exception {
        fragment = DeliveriesFragment.newInstance();
        fragment.setDeliveriesPresenter(presenter);
        startFragment(fragment);
        assertNotNull(fragment);

        verify(presenter).managePermissions();
    }

    @Test
    public void onDestroyView() throws Exception {
        fragment = DeliveriesFragment.newInstance();
        fragment.setDeliveriesPresenter(presenter);
        startFragment(fragment);
        assertNotNull(fragment);

        fragment.onDestroyView();

        verify(presenter).detachView();
    }

    @Test
    public void showDeliveriesShouldProccessOk() throws Exception {
        fragment = DeliveriesFragment.newInstance();
        fragment.setDeliveriesPresenter(presenter);
        startFragment(fragment);
        assertNotNull(fragment);
        List<Business> list = Collections.singletonList(business);
        fragment.showDeliveries(new ArrayList<>(list));

        assertEquals(fragment.getBusinessAdapter().getBusinessList(), list);
    }

    @Test
    public void showDeliveriesShouldDoNothing() throws Exception {
        fragment = DeliveriesFragment.newInstance();
        fragment.setDeliveriesPresenter(presenter);
        startFragment(fragment);
        assertNotNull(fragment);

        fragment.showDeliveries(null);

        assertEquals(fragment.getBusinessAdapter().getBusinessList().size(), 0);
    }

}