package com.example.pawel_piedel.thesis.data.auth.remote;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by PPiedel on 20.01.2018.
 */

public interface AccessTokenApiService {
    @FormUrlEncoded
    @POST("/oauth2/token")
    Observable<AccessToken> loadAccessToken(@Field("grant_type") String grantType,
                                            @Field("client_id") String clientId,
                                            @Field("client_secret") String clientSecret);
}
