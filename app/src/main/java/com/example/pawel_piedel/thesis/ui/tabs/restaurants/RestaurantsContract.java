package com.example.pawel_piedel.thesis.ui.tabs.restaurants;

import android.content.Context;

import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.example.pawel_piedel.thesis.data.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

public interface RestaurantsContract {
    interface View extends BaseView {

        void showRestaurants(List<Business> restaurants);

        Context provideContext();
    }


    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {

        void onViewPrepared();

        void load();

        void manageToLoadRestaurants();
    }
}
