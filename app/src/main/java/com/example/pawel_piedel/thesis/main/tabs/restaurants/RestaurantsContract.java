package com.example.pawel_piedel.thesis.main.tabs.restaurants;

import android.content.Context;

import com.example.pawel_piedel.thesis.BasePresenter;
import com.example.pawel_piedel.thesis.BaseView;
import com.example.pawel_piedel.thesis.main.tabs.cafes.CafesContract;
import com.example.pawel_piedel.thesis.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

public interface RestaurantsContract {
    interface View extends BaseView<RestaurantsContract.Presenter> {
        void showRestaurants(List<Business> restaurants);
        Context provideContext();
    }


    interface Presenter extends BasePresenter {
        void onViewPrepared();
        void load();
        void manageToLoadRestaurants();
    }
}
