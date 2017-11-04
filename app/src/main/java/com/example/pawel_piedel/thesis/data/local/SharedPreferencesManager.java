package com.example.pawel_piedel.thesis.data.local;

import android.content.SharedPreferences;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

import javax.inject.Inject;

import static com.example.pawel_piedel.thesis.util.Util.gson;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

public class SharedPreferencesManager implements LocalDataSource {
    public static final String DEFAULT_STRING_IF_NOT_FOUND = "Not found";
    private static final String ACCESS_TOKEN = "access_token";
    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public AccessToken getAccessToken() {
        String json = sharedPreferences
                .getString(ACCESS_TOKEN, "");
        return gson.fromJson(json, AccessToken.class);
    }

    @Override
    public void saveObject(Object object) {
        sharedPreferences
                .edit()
                .putString("access_token", gson.toJson(object))
                .apply();

    }

    @Override
    public void saveString(String stringToSave, String label) {
        sharedPreferences.edit().putString(label, stringToSave).apply();
    }

    @Override
    public void putBoolean(String label, boolean value) {
        sharedPreferences.edit().putBoolean(label, value).apply();
    }

    @Override
    public boolean getBoolean(String label, boolean forValue) {
        return sharedPreferences.getBoolean(label, forValue);
    }

    @Override
    public String getString(String label) {
        return sharedPreferences.getString(label, DEFAULT_STRING_IF_NOT_FOUND);
    }

    @Override
    public void removeFromSharedPreferences(String label) {
        sharedPreferences.edit().remove(label).apply();
    }

}
