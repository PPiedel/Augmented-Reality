package com.example.pawel_piedel.thesis.main;

import android.support.annotation.NonNull;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public class MainPresenter implements MainContract.Presenter {
    private static final String LOG_TAG = MainPresenter.class.getName();
    private MainContract.View mainView;

    public MainPresenter(@NonNull MainContract.View mainView) {
        this.mainView = mainView;
        mainView.setPresenter(this);
    }

    @Override
    public void start() {
    //    loadAccessToken();
    }

   /* @Override
    public void loadAccessToken() {
        try {
            ServiceFactory.loadAccessToken(new GetAccessTokenCallback() {
                @Override
                public void onSuccess(AccessToken accessToken) {
                    Log.v(LOG_TAG,accessToken.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(LOG_TAG,t.toString());
                }

                @Override
                public void onNetworkFaiulre() {
                    Log.e(LOG_TAG,"Network failure");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
