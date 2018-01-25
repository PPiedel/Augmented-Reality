package com.example.pawel_piedel.thesis.data.auth.local;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

/**
 * Created by PPiedel on 20.01.2018.
 */

public interface AccessTokenLocalDataSource {
    AccessToken loadAccessToken();

    void saveAccessToken(AccessToken accessToken);
}
