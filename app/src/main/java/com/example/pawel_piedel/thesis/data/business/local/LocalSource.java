package com.example.pawel_piedel.thesis.data.business.local;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

public class LocalSource implements LocalDataSource {
    public static final String DEFAULT_STRING_IF_NOT_FOUND = "Not found";

    private final SharedPreferences sharedPreferences;

    @Inject
    public LocalSource(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
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
