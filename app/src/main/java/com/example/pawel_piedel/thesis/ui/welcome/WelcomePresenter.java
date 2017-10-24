package com.example.pawel_piedel.thesis.ui.welcome;

import android.util.Log;
import android.view.View;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;

import javax.inject.Inject;

import static android.support.v4.content.res.TypedArrayUtils.getString;

/**
 * Created by Pawel_Piedel on 22.10.2017.
 */
@ConfigPersistent
public class WelcomePresenter <V extends WelcomeContract.View> extends BasePresenter<V> implements WelcomeContract.Presenter<V> {
    private static final String lOG_TAG = WelcomePresenter.class.getSimpleName();

    @Inject
    public WelcomePresenter(DataManager dataManager){
        super(dataManager);
    }

    @Override
    public void onViewDuringCreation() {
        if (getDataManager().isFirstTimeLunched()){
            getView().initLayout();
            getDataManager().saveInSharedPreferences(DataManager.FIRST_TIME_LUNCHED,false);
        }
        else {
            getView().startMainActivity();
        }
    }

    @Override
    public void onSkipButtonClicked() {
        getView().startMainActivity();
    }

    @Override
    public void onNextButtonClicked(int currentPage) {
        Log.d(lOG_TAG,"On next button clicked, current page : "+currentPage);
        if (currentPage+1 < getView().getLayoutsLength()) {
            getView().moveToNextScreen();
        } else {
            getView().startMainActivity();
        }
    }

    @Override
    public void onPageSelected(int position) {
        getView().addBottomDots(position);

        if (position == getView().getLayoutsLength() - 1) {
            getView().setNextButtonText("Zaczynajmy");
            getView().setSkipButtonVisibility(View.GONE);
        } else {
            getView().setNextButtonText("Następny");
            getView().setSkipButtonVisibility(View.VISIBLE);
        }
    }
}
