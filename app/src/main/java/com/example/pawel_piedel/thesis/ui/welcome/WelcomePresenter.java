package com.example.pawel_piedel.thesis.ui.welcome;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 22.10.2017.
 */

public class WelcomePresenter <V extends WelcomeContract.View> extends BasePresenter {

    @Inject
    public WelcomePresenter(DataManager dataManager){
        super(dataManager);
    }
}
