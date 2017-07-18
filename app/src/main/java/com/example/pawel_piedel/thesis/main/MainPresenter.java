package com.example.pawel_piedel.thesis.main;

import android.support.annotation.NonNull;

import com.example.pawel_piedel.thesis.api.ApiService;
import com.example.pawel_piedel.thesis.api.ServiceGenerator;

import static com.example.pawel_piedel.thesis.api.ServiceGenerator.CLIENT_ID;
import static com.example.pawel_piedel.thesis.api.ServiceGenerator.CLIENT_SECRET;
import static com.example.pawel_piedel.thesis.api.ServiceGenerator.GRANT_TYPE;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mainView;
    private ApiService apiService;

    public MainPresenter(@NonNull MainContract.View mainView) {
        this.mainView = mainView;
        mainView.setPresenter(this);
    }

    @Override
    public void start() {
        loadAccessToken();
    }

    @Override
    public void loadAccessToken() {
        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.provideAccessToken(CLIENT_ID,CLIENT_SECRET,GRANT_TYPE);
    }
}
