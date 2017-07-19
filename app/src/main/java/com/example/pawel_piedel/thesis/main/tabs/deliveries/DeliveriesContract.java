package com.example.pawel_piedel.thesis.main.tabs.deliveries;

import com.example.pawel_piedel.thesis.BasePresenter;
import com.example.pawel_piedel.thesis.BaseView;
import com.example.pawel_piedel.thesis.main.tabs.cafes.CafesContract;
import com.example.pawel_piedel.thesis.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */

public interface DeliveriesContract {

    interface View extends BaseView<Presenter> {
        void showDeliveries(List<Business> deliveries);

    }

    interface Presenter extends BasePresenter {
        void load();
        void loadDeliveries();
    }
}
