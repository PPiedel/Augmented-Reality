package com.example.pawel_piedel.thesis.api;

import com.example.pawel_piedel.thesis.model.Business;
import com.example.pawel_piedel.thesis.model.SearchResponse;
import com.example.pawel_piedel.thesis.model.AccessToken;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Pawel_Piedel on 04.07.2017.
 */

public interface ApiService {
    public final String BUSINESS_ID_PARAM = "id";

    @GET("/v3/businesses/search")
    Observable<SearchResponse> getBusinessesList(@Query("latitude") double latitiude, @Query("longitude") double longtitiude);

    @GET("/businesses/{id}")
    Observable<Business> getBusinessDetails(@Path(BUSINESS_ID_PARAM) String id);

    @FormUrlEncoded
    @POST("/oauth2/token")
    Observable<AccessToken> getAccessToken(@Field("client_id") String clientId,
                                           @Field("client_secret") String clientSecret,
                                           @Field("grant_type") String grantType);


}
