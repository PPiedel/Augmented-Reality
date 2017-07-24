package com.example.pawel_piedel.thesis.ui.augumented_reality;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.ui.base.BaseView;

/**
 * Created by Pawel_Piedel on 24.07.2017.
 */

public interface ARContract {
    interface View extends BaseView{
        void showBusinessOnScreen(Business business);
    }

    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {

    }
}
