package com.example.pawel_piedel.thesis.data.business.remote;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.data.model.SearchResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by PPiedel on 20.01.2018.
 */

public class BusinessRemoteSource implements BusinessRemoteDataSource {
    private BusinessApiService businessApiService;

    @Inject
    public BusinessRemoteSource(BusinessApiService businessApiService) {
        this.businessApiService = businessApiService;
    }

    @Override
    public Observable<SearchResponse> loadBusinesses(String term, double latitiude, double longtitiude) {
        return businessApiService.loadBusinesses(term, latitiude, longtitiude);
    }

    @Override
    public Observable<Business> loadBusinessDetails(String id) {
        return businessApiService.getBusinessDetails(id);
    }

    @Override
    public Observable<ReviewsResponse> getBusinessReviews(String id, String locale) {
        return businessApiService.getBusinessReviews(id, locale);
    }
}
