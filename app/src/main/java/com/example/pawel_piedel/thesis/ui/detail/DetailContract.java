package com.example.pawel_piedel.thesis.ui.detail;

import android.app.Activity;

import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.Review;
import com.example.pawel_piedel.thesis.ui.base.BaseView;

import java.util.List;

/**
 * Created by Pawel_Piedel on 27.07.2017.
 */

public interface DetailContract {
    interface View extends BaseView {

        Activity getViewActivity();

        void setUpBusiness(Business business);

        void showBusinessDetails(Business business);

        void showReviews(List<Review> reviews);

        void showOldBusiness();

        void getOldBusinessFromIntent();

        void onCallButtonClicked();

        void onWebsiteButtonclicked();

        void onFavouriteButtonClicked();

        void makeCall(Business business);

    }

    interface Presenter<V extends BaseView> extends com.example.pawel_piedel.thesis.ui.base.Presenter<V> {
        void loadBusinessDetails(String id);

        void manageToLoadReviews(String id);

        void onViewPrepared(String businessId);

        void onCallButtonClicked(Business business);

        void goToWebsite(Business business);

        void addToFavourite(Business business);

    }
}
