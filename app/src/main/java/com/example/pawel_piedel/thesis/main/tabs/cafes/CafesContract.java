package com.example.pawel_piedel.thesis.main.tabs.cafes;

import android.support.annotation.NonNull;

import com.example.pawel_piedel.thesis.BaseView;
import com.example.pawel_piedel.thesis.BasePresenter;
import com.example.pawel_piedel.thesis.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface CafesContract {
    interface View extends BaseView<Presenter>{
        void showCafes(List<Business> cafes);

    }


    interface Presenter extends BasePresenter{
        void getAccessToken();
        void loadCafes();
    }

}
