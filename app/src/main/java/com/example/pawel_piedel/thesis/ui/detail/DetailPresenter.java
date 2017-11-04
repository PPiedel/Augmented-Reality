package com.example.pawel_piedel.thesis.ui.detail;

import android.Manifest;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.BusinessDataSource;
import com.example.pawel_piedel.thesis.data.local.SharedPreferencesManager;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.Review;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.Objects;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Pawel_Piedel on 27.07.2017.
 */

@ConfigPersistent
public class DetailPresenter<V extends DetailContract.View> extends BasePresenter<V> implements DetailContract.Presenter<V> {
    private final static String LOG_TAG = DetailPresenter.class.getSimpleName();

    @Inject
    DetailPresenter(BusinessDataSource businessDataSource) {
        super(businessDataSource);
    }

    @Override
    public void loadBusinessDetails(String id) {
        getView().showProgressDialog();
        getBusinessDataSource().loadAccessToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccessToken>() {
                    @Override
                    public void onCompleted() {
                        loadBusiness(id);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AccessToken accessToken) {

                    }
                });
    }

    @Override
    public void manageToLoadReviews(String id) {
        getBusinessDataSource().loadAccessToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccessToken>() {
                    @Override
                    public void onCompleted() {
                        loadReviews(id);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AccessToken accessToken) {

                    }
                });

    }

    private void loadReviews(String id) {
        getBusinessDataSource().loadReviews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ReviewsResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ReviewsResponse reviewsResponse) {
                        Log.d(LOG_TAG, "Total : " + reviewsResponse.getTotal());
                        for (Review review : reviewsResponse.getReviews()) {
                            Log.d(LOG_TAG, review.toString());
                        }
                        getView().showReviews(reviewsResponse.getReviews());
                    }
                });
    }


    private void loadBusiness(String id) {
        getBusinessDataSource().loadBusinessDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Business>() {
                    @Override
                    public void onCompleted() {
                        manageToLoadReviews(id);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressDialog();
                        Log.d(LOG_TAG, e.getMessage());
                        getView().showOldBusiness();
                    }

                    @Override
                    public void onNext(Business business) {
                        Log.d(LOG_TAG, business.toString());
                        getView().hideProgressDialog();
                        getView().setUpBusiness(business);
                        getView().showBusinessDetails(business);
                    }

                });
    }


    @Override
    public void onViewPrepared(String businessId) {
        loadBusinessDetails(businessId);
    }

    @Override
    public void onCallButtonClicked(Business business) {
        RxPermissions rxPermissions = new RxPermissions(getView().getViewActivity());
        rxPermissions
                .request(Manifest.permission.CALL_PHONE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        getView().makeCall(business);
                    }
                });

    }

    @Override
    public void onWebsiteButtonClicked(Business business) {
        getView().goToWebsite(business);

    }


    public void showFavouriteIcon(Business business) {
        if (isSaved(business)) {
            getView().fillFavouriteIcon();
        } else {
            getView().showBorderIcon();
        }
    }

    private boolean isSaved(Business business) {
        boolean isSaved = false;
        if (!Objects.equals(getBusinessDataSource().getFromSharedPreferences(business.getId()), SharedPreferencesManager.DEFAULT_STRING_IF_NOT_FOUND)) {
            isSaved = true;
        }
        return isSaved;
    }

    @Override
    public void addOrRemoveFromFavourites(Business business) {
        if (isSaved(business)) {
            removeFromFavourite(business);
        } else {
            addToFavourites(business);
        }
        showFavouriteIcon(business);
    }

    public void addToFavourites(Business business) {
        getBusinessDataSource().saveInSharedPreferences(business.getId(), business.getId());
    }

    private void removeFromFavourite(Business business) {
        getBusinessDataSource().removeFromSharedPreferences(business.getId());
    }


}
