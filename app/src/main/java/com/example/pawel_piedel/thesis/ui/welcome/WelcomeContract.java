package com.example.pawel_piedel.thesis.ui.welcome;

import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.example.pawel_piedel.thesis.ui.base.BaseView;

/**
 * Created by Pawel_Piedel on 22.10.2017.
 */

public interface WelcomeContract {
    interface View extends BaseView {

    }

    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V>{

    }
}
