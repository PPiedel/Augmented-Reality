package com.example.pawel_piedel.thesis.data.auth.remote;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by PPiedel on 20.01.2018.
 */
public class AccessTokenRemoteSourcempl implements AccessTokenRemoteSource {
    private AccessTokenApiService accessTokenApiService;

    @Inject
    public AccessTokenRemoteSourcempl(AccessTokenApiService accessTokenApiService) {
        this.accessTokenApiService = accessTokenApiService;
    }

    @Override
    public Observable<AccessToken> loadAccessToken(String grantType, String clientId, String clientSecret) {
        return accessTokenApiService.loadAccessToken(grantType, clientId, clientSecret);
    }
}
