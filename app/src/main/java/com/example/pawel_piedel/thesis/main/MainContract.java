package com.example.pawel_piedel.thesis.main;

import com.example.pawel_piedel.thesis.BasePresenter;
import com.example.pawel_piedel.thesis.BaseView;
import com.example.pawel_piedel.thesis.main.tabs.cafes.CafesContract;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface MainContract  {
    public interface View extends BaseView<Presenter>{

    }


    public interface Presenter extends BasePresenter {
        //void loadAccessToken();
    }
}
