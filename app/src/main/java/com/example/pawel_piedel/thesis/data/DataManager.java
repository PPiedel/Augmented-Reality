package com.example.pawel_piedel.thesis.data;

import android.util.Log;

import com.example.pawel_piedel.thesis.data.local.SharedPreferencesHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */
@Singleton
public class DataManager {
    private final String LOG_TAG = DataManager.class.getSimpleName();
    private final SharedPreferencesHelper preferencesHelper;

    @Inject
    public DataManager(SharedPreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public SharedPreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public void test(){
        Log.v(LOG_TAG,"Test");
    }
}
