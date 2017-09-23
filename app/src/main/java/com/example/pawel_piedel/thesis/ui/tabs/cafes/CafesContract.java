package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.app.Activity;
import android.content.Context;

import com.example.pawel_piedel.thesis.ui.base.BaseView;
import com.example.pawel_piedel.thesis.data.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface CafesContract {
    interface View extends BaseView {

        void showCafes(List<Business> cafes);

        Activity getViewActivity();
    }


    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {
        void managePermissions();
        void onViewPrepared();

    }

}
