package com.example.pawel_piedel.thesis.data.auth;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

import rx.Observable;

/**
 * Created by PPiedel on 20.01.2018.
 */

public interface AccessTokenRepository {
    Observable<AccessToken> loadAccessToken();

    void saveAccessToken(AccessToken accessToken);
}
