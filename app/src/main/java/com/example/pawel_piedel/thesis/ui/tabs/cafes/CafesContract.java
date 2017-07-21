package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.content.Context;

import com.example.pawel_piedel.thesis.BaseView;
import com.example.pawel_piedel.thesis.data.model.Business;

import java.util.List;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface CafesContract {
    interface View extends BaseView<Presenter>{
        void showCafes(List<Business> cafes);
        Context provideContext();
    }


    interface Presenter extends com.example.pawel_piedel.thesis.Presenter {
        void onViewPrepared();
        void load();
        void manageToLoadCafes();
    }

}
