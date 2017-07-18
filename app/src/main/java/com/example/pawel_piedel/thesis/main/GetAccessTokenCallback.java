package com.example.pawel_piedel.thesis.main;

import com.example.pawel_piedel.thesis.model.AccessToken;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface GetAccessTokenCallback {
    void onSuccess(AccessToken accessToken);

    void onFailure(Throwable t);

    void onNetworkFaiulre();

}
