package com.example.pawel_piedel.thesis.data;

import android.annotation.SuppressLint;
import android.test.mock.MockContext;

import com.example.pawel_piedel.thesis.data.local.SharedPreferencesManager;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.remote.ApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.verify;

/**
 * Created by Pawel_Piedel on 01.09.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock
    SharedPreferencesManager sharedPreferencesManager;

    @Mock
    ApiService apiService;

    DataManager dataManager;

    @Before
    public void setUp(){
        dataManager = new DataManager(new MockContext(),apiService,sharedPreferencesManager);
    }



    @Test
    public void getPreferencesHelper() throws Exception {
    }

    @Test
    public void getAccessToken() throws Exception {
    }

    @Test
    public void getLastKnownLocation() throws Exception {
    }

    @SuppressLint("MissingPermission")
    @Test
    public void getLocationUpdates() throws Exception {
    }

    @Test
    public void safelyUnsubscribe() throws Exception {
    }

    @Test
    public void loadBusinesses() throws Exception {
    }

    @Test
    public void loadBusinessDetails() throws Exception {
    }

    @Test
    public void loadReviews() throws Exception {
    }

    @Test
    public void saveAccessToken() throws Exception {
        AccessToken accessToken = Mockito.mock(AccessToken.class);
        dataManager.saveAccessToken(accessToken);

        verify(sharedPreferencesManager).saveAccessToken(accessToken);

    }

    @Test
    public void saveLocation() throws Exception {
    }

    @Test
    public void saveBusinesses() throws Exception {
    }

}