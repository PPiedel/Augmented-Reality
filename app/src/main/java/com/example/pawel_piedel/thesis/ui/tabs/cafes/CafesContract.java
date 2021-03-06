package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.example.pawel_piedel.thesis.ui.main.BusinessAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface CafesContract {
    interface View extends BaseView {

        void showCafes(List<Business> cafes);

        RxPermissions getRxPermissions();

        void setRxPermissions(RxPermissions rxPermissions);

        void setCafesPresenter(CafesContract.Presenter<CafesContract.View> cafesPresenter);

        BusinessAdapter getBusinessAdapter();

    }


    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {
        void managePermissions();

        void clearSubscriptions();
    }

}
