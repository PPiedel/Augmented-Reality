package com.example.pawel_piedel.thesis.main;

import com.example.pawel_piedel.thesis.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface GetBusinessesCallback {
    void onSuccess(List<Business> businesses);

    void onFailure(Throwable t);

    void onNetworkFaiulre();
}
