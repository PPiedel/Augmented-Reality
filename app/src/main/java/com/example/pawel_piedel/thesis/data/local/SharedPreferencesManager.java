package com.example.pawel_piedel.thesis.data.local;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.example.pawel_piedel.thesis.util.Util.gson;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

@Singleton
public class SharedPreferencesManager {
    private static final String ACCESS_TOKEN = "access_token";
    private final String LOG_TAG = SharedPreferencesManager.class.getSimpleName();

    private final SharedPreferences sharedPreferences;

    @Inject
     SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public AccessToken getAccessToken() {
        String json = sharedPreferences
                .getString(ACCESS_TOKEN, "");
       // Log.v(LOG_TAG, "Access token from Shared Preferences : " + json);
        return gson.fromJson(json, AccessToken.class);
    }

    public void saveAccessToken(AccessToken accessToken) {
        sharedPreferences
                .edit()
                .putString("access_token", gson.toJson(accessToken))
                .apply();

    }

}
