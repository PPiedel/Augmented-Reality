package com.example.pawel_piedel.thesis.data.auth.local;

import android.content.SharedPreferences;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.example.pawel_piedel.thesis.util.Util.gson;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by PPiedel on 20.01.2018.
 */
public class AccessTokenLocalDataSourceImplTest {
    @Mock
    SharedPreferences sharedPreferences;

    AccessTokenLocalDataSourceImpl authLocalDataSource;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        authLocalDataSource = new AccessTokenLocalDataSourceImpl(sharedPreferences);
    }

    @Test
    public void loadAccessToken() throws Exception {
        //given
        AccessToken accessToken = new AccessToken("test", "test", 0);
        String accessTokenJson = gson.toJson(accessToken);
        String testTokenParam = AccessTokenLocalDataSourceImpl.ACCESS_TOKEN_PARAM;
        when(sharedPreferences.getString(testTokenParam, anyString())).thenReturn(accessTokenJson);

        //when
        AccessToken receivedToken = authLocalDataSource.loadAccessToken();

        //thrn
        assertEquals(accessToken, receivedToken);
    }

    @Test
    public void saveAccessToken() throws Exception {
        //given
        AccessToken accessToken = new AccessToken("test", "test", 0);

        //when
        authLocalDataSource.saveAccessToken(accessToken);


        //then
        String json = sharedPreferences.getString(AccessTokenLocalDataSourceImpl.ACCESS_TOKEN_PARAM, "");
        assertEquals(accessToken, gson.fromJson(json, AccessToken.class));
    }

}