package com.example.pawel_piedel.thesis.data.auth.remote;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

import rx.Observable;

/**
 * Created by PPiedel on 20.01.2018.
 */

public interface AccessTokenRemoteSource {
    Observable<AccessToken> loadAccessToken(String grantType, String clientId, String clientSecret);
}
