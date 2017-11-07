package com.example.pawel_piedel.thesis.ui.tabs.restaurants;

import android.app.Activity;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.example.pawel_piedel.thesis.ui.main.BusinessAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import rx.Observable;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

public interface RestaurantsContract {
    interface View extends BaseView {

        void showRestaurants(List<Business> list);

        Activity getParentActivity();

        RxPermissions getRxPermissions();

        void setRxPermissions(RxPermissions rxPermissions);

        BusinessAdapter getBusinessAdapter();

        void setRestaurantsPresenter(RestaurantsContract.Presenter<RestaurantsContract.View> restaurantsPresenter);
    }


    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {

        void onViewPrepared();

        void loadRestaurannts();

        void managePermissions();

        Observable<Boolean> requestPermissions();
    }
}
