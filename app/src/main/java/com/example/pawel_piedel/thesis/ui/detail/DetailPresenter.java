package com.example.pawel_piedel.thesis.ui.detail;

import android.Manifest;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.auth.AccessTokenRepository;
import com.example.pawel_piedel.thesis.data.business.BusinessRepository;
import com.example.pawel_piedel.thesis.data.business.local.LocalSource;
import com.example.pawel_piedel.thesis.data.location.LocationRepository;
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
    DetailPresenter(BusinessRepository businessRepository, LocationRepository locationRepository, AccessTokenRepository accessTokenRepository) {
        super(businessRepository, locationRepository, accessTokenRepository);
    }

    @Override
    public void loadBusinessDetails(String id) {
        view.showProgressDialog();
        accessTokenRepository.loadAccessToken()
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
        accessTokenRepository.loadAccessToken()
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
        businessRepository.loadReviews(id)
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
                        view.showReviews(reviewsResponse.getReviews());
                    }
                });
    }


    private void loadBusiness(String id) {
        businessRepository.loadBusinessDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Business>() {
                    @Override
                    public void onCompleted() {
                        manageToLoadReviews(id);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProgressDialog();
                        Log.d(LOG_TAG, e.getMessage());
                        view.showOldBusiness();
                    }

                    @Override
                    public void onNext(Business business) {
                        Log.d(LOG_TAG, business.toString());
                        view.hideProgressDialog();
                        view.setUpBusiness(business);
                        view.showBusinessDetails(business);
                    }

                });
    }


    @Override
    public void onViewPrepared(String businessId) {
        loadBusinessDetails(businessId);
    }

    @Override
    public void onCallButtonClicked(Business business) {
        RxPermissions rxPermissions = new RxPermissions(view.getViewActivity());
        rxPermissions
                .request(Manifest.permission.CALL_PHONE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        view.makeCall(business);
                    }
                });

    }

    @Override
    public void onWebsiteButtonClicked(Business business) {
        view.goToWebsite(business);

    }


    public void showFavouriteIcon(Business business) {
        if (isSaved(business)) {
            view.fillFavouriteIcon();
        } else {
            view.showBorderIcon();
        }
    }

    private boolean isSaved(Business business) {
        boolean isSaved = false;
        if (!Objects.equals(businessRepository.getFromSharedPreferences(business.getId()), LocalSource.DEFAULT_STRING_IF_NOT_FOUND)) {
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
        businessRepository.saveInSharedPreferences(business.getId(), business.getId());
    }

    private void removeFromFavourite(Business business) {
        businessRepository.removeFromSharedPreferences(business.getId());
    }


}
