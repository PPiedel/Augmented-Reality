package com.example.pawel_piedel.thesis.ui.tabs.deliveries;

import android.content.Context;

import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.example.pawel_piedel.thesis.data.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */

public interface DeliveriesContract {

    interface View extends BaseView {
        void showDeliveries(List<Business> deliveries);
        Context provideContext();
    }


    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {
        void onViewPrepared();
        void load();
        void manageToLoadDeliveries();
    }
}
