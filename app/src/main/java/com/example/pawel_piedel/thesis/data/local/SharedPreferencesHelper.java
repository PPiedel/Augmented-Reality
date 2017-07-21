package com.example.pawel_piedel.thesis.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pawel_piedel.thesis.injection.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.http.Field;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

@Singleton
public class SharedPreferencesHelper {
    public static final String FILE_NAME = "access_token";

    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesHelper(@ApplicationContext Context context){
        sharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
