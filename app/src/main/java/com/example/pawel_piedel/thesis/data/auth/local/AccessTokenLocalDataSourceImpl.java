package com.example.pawel_piedel.thesis.data.auth.local;

import android.content.SharedPreferences;

import com.example.pawel_piedel.thesis.data.auth.remote.AuthenticationInterceptor;
import com.example.pawel_piedel.thesis.data.model.AccessToken;

import javax.inject.Inject;

import static com.example.pawel_piedel.thesis.util.Util.gson;

/**
 * Created by PPiedel on 20.01.2018.
 */

public class AccessTokenLocalDataSourceImpl implements AccessTokenLocalDataSource {
    public static final String ACCESS_TOKEN_PARAM = "access_token";
    private final SharedPreferences sharedPreferences;
    private final AuthenticationInterceptor authenticationInterceptor;

    @Inject
    public AccessTokenLocalDataSourceImpl(SharedPreferences sharedPreferences, AuthenticationInterceptor authenticationInterceptor) {
        this.sharedPreferences = sharedPreferences;
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public AccessToken loadAccessToken() {
        String json = sharedPreferences
                .getString(ACCESS_TOKEN_PARAM, "");
        return gson.fromJson(json, AccessToken.class);
    }

    @Override
    public void saveAccessToken(AccessToken accessToken) {
        sharedPreferences
                .edit()
                .putString("access_token", gson.toJson(accessToken))
                .apply();

        authenticationInterceptor.setAccessToken(accessToken);

    }
}
