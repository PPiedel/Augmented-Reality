package com.example.pawel_piedel.thesis.ui.tabs.deliveries;

import android.app.Activity;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.example.pawel_piedel.thesis.ui.main.BusinessAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

/**
 * Created by Pawel_Piedel on 19.07.2017.
 */

public interface DeliveriesContract {

    interface View extends BaseView {

        void showDeliveries(List<Business> deliveries);

        Activity getParentActivity();

        RxPermissions getRxPermissions();

        void setRxPermissions(RxPermissions rxPermissions);

        void setDeliveriesPresenter(DeliveriesContract.Presenter<DeliveriesContract.View> deliveriesPresenter);

        BusinessAdapter getBusinessAdapter();
    }


    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {

        void managePermissions();

        void loadDeliveries();

        void clearSubscriptions();
    }
}
