package com.example.pawel_piedel.thesis.ui.tabs.deliveries;

import android.content.Context;

import com.example.pawel_piedel.thesis.BaseView;
import com.example.pawel_piedel.thesis.data.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */

public interface DeliveriesContract {

    interface View extends BaseView<Presenter> {
        void showDeliveries(List<Business> deliveries);
        Context provideContext();
    }


    interface Presenter extends com.example.pawel_piedel.thesis.Presenter {
        void onViewPrepared();
        void load();
        void manageToLoadDeliveries();
    }
}
