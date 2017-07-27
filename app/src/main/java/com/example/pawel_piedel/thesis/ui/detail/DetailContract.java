package com.example.pawel_piedel.thesis.ui.detail;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseView;

/**
 * Created by Pawel_Piedel on 27.07.2017.
 */

public interface DetailContract {
    public interface View extends BaseView{
        void showBusinessImage();
        void getBusinessFromIntent();
        void setUpTitle();

    }

    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V>{
        void showBusinessImage();
        void onViewPrepared();

    }
}
