package com.example.pawel_piedel.thesis.data.business.remote;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;

import rx.Observable;

/**
 * Created by PPiedel on 20.01.2018.
 */

public interface BusinessRemoteDataSource {
    Observable<SearchResponse> loadBusinesses(String term, double latitiude, double longtitiude);

    Observable<Business> loadBusinessDetails(String id);

    Observable<ReviewsResponse> getBusinessReviews(String id, String locale);
}
