package com.example.pawel_piedel.thesis.ui.welcome;

import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.example.pawel_piedel.thesis.ui.base.BaseView;

/**
 * Created by Pawel_Piedel on 22.10.2017.
 */

public interface WelcomeContract {
    interface View extends BaseView {
        void startMainActivity();
        void initLayout();
        void moveToNextScreen();
        int getLayoutsLength();
        void addBottomDots(int position);
        void setSkipButtonVisibility(int visibility);
        void setNextButtonText(String text);
    }

    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V>{
        void onViewDuringCreation();
        void onSkipButtonClicked();
        void onNextButtonClicked(int currentPage);
        void onPageSelected(int position);

    }
}
