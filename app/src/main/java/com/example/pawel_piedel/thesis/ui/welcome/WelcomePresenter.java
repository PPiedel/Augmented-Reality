package com.example.pawel_piedel.thesis.ui.welcome;

import android.util.Log;
import android.view.View;

import com.example.pawel_piedel.thesis.data.auth.AccessTokenRepository;
import com.example.pawel_piedel.thesis.data.business.BusinessRepository;
import com.example.pawel_piedel.thesis.data.business.BusinessRepositoryImpl;
import com.example.pawel_piedel.thesis.data.location.LocationRepository;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 22.10.2017.
 */
@ConfigPersistent
public class WelcomePresenter <V extends WelcomeContract.View> extends BasePresenter<V> implements WelcomeContract.Presenter<V> {
    private static final String lOG_TAG = WelcomePresenter.class.getSimpleName();

    @Inject
    public WelcomePresenter(BusinessRepository businessRepositoryImpl, LocationRepository locationRepository, AccessTokenRepository accessTokenRepository) {
        super(businessRepositoryImpl, locationRepository, accessTokenRepository);
    }

    @Override
    public void onViewDuringCreation() {
        if (businessRepository.isFirstTimeLunched()) {
            view.initLayout();
            businessRepository.saveInSharedPreferences(BusinessRepositoryImpl.FIRST_TIME_LUNCHED, false);
        }
        else {
            view.startMainActivity();
        }
    }

    @Override
    public void onSkipButtonClicked() {
        view.startMainActivity();
    }

    @Override
    public void onNextButtonClicked(int currentPage) {
        Log.d(lOG_TAG,"On next button clicked, current page : "+currentPage);
        if (currentPage + 1 < view.getLayoutsLength()) {
            view.moveToNextScreen();
        } else {
            view.startMainActivity();
        }
    }

    @Override
    public void onPageSelected(int position) {
        view.addBottomDots(position);

        if (position == view.getLayoutsLength() - 1) {
            view.setNextButtonText("Zaczynajmy");
            view.setSkipButtonVisibility(View.GONE);
        } else {
            view.setNextButtonText("Następny");
            view.setSkipButtonVisibility(View.VISIBLE);
        }
    }
}
