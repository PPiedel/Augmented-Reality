package com.example.pawel_piedel.thesis.data.auth;

import com.example.pawel_piedel.thesis.data.auth.local.AccessTokenLocalDataSource;
import com.example.pawel_piedel.thesis.data.auth.remote.AccessTokenRemoteSource;
import com.example.pawel_piedel.thesis.data.model.AccessToken;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by PPiedel on 20.01.2018.
 */
public class AccessTokenRepositoryImpl implements AccessTokenRepository {
    public static final String CLIENT_ID = "VokcbDNJly63jzOhJqJ0JA";
    public static final String CLIENT_SECRET = "gaFo3VLh1cNWS5L7nHJ6nRxVq97iRJCqvBAWnvmoiAWCf2xriOKhp6h5U0LNuj8F";
    public static final String GRANT_TYPE = "client_credentials";

    private AccessTokenRemoteSource accessTokenRemoteSource;
    private AccessTokenLocalDataSource authLocalSource;

    @Inject
    public AccessTokenRepositoryImpl(AccessTokenLocalDataSource authLocalSource, AccessTokenRemoteSource accessTokenRemoteSource) {
        this.accessTokenRemoteSource = accessTokenRemoteSource;
        this.authLocalSource = authLocalSource;
    }

    @Override
    public Observable<AccessToken> loadAccessToken() {
        AccessToken accessToken = authLocalSource.loadAccessToken();
        if (accessToken == null) {
            return accessTokenRemoteSource.loadAccessToken(GRANT_TYPE, CLIENT_ID, CLIENT_SECRET);
        } else {
            return Observable.just(accessToken);
        }
    }

    @Override
    public void saveAccessToken(AccessToken accessToken) {
        authLocalSource.saveAccessToken(accessToken);
    }
}
