package com.example.pawel_piedel.thesis.data.local;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

/**
 * Created by Pawel_Piedel on 04.11.2017.
 */

public interface LocalDataSource {
    AccessToken getAccessToken();

    void saveObject(Object object);

    void saveString(String stringToSave, String label);

    void putBoolean(String label, boolean value);

    boolean getBoolean(String label, boolean forValue);

    String getString(String label);

    void removeFromSharedPreferences(String label);

}
