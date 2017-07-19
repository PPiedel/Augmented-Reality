package com.example.pawel_piedel.thesis;

import com.example.pawel_piedel.thesis.model.AccessToken;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface BasePresenter {

    void start();
    void saveAccessTokenInSharedPref();
    AccessToken retrieveAccessTokenFromSharedPref();

}
