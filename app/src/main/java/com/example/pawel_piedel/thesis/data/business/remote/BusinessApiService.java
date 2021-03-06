package com.example.pawel_piedel.thesis.data.business.remote;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Pawel_Piedel on 04.07.2017.
 */

public interface BusinessApiService {
    String BUSINESS_ID_PARAM = "id";

    @GET("/v3/businesses/search")
    Observable<SearchResponse> loadBusinesses(
            @Query("term") String term,
            @Query("latitude") double latitiude,
            @Query("longitude") double longtitiude);

    @GET("/v3/businesses/{id}")
    Observable<Business> getBusinessDetails(@Path(BUSINESS_ID_PARAM) String id);

    @GET("/v3/businesses/{id}/reviews")
    Observable<ReviewsResponse> getBusinessReviews(@Path(BUSINESS_ID_PARAM) String id, @Query("locale") String locale);

}
