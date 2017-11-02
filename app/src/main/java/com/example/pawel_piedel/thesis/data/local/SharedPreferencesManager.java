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
    public static final String DEFAULT_STRING_IF_NOT_FOUND = "Not found";
    private final SharedPreferences sharedPreferences;

    @Inject
    SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public AccessToken getAccessToken() {
        String json = sharedPreferences
                .getString(ACCESS_TOKEN, "");
        return gson.fromJson(json, AccessToken.class);
    }

    public void saveObject(Object accessToken) {
        sharedPreferences
                .edit()
                .putString("access_token", gson.toJson(accessToken))
                .apply();

    }

    public void saveString(String stringToSave, String label) {
        sharedPreferences.edit().putString(label, stringToSave).apply();
    }

    public void putBoolean(String label, boolean value) {
        sharedPreferences.edit().putBoolean(label, value).apply();
    }

    public boolean getBoolean(String label, boolean forValue) {
        return sharedPreferences.getBoolean(label, forValue);
    }


    public String getString(String label) {
        return sharedPreferences.getString(label, DEFAULT_STRING_IF_NOT_FOUND);
    }

    public void removeFromSharedPreferences(String label) {
        sharedPreferences.edit().remove(label).apply();
    }

}
