package com.example.pawel_piedel.thesis.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.model.AccessToken;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.Review;
import com.example.pawel_piedel.thesis.data.model.ReviewsResponse;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;

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
    private Business business;

    @Inject
    DetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void loadBusinessDetails(String id) {
        getView().showProgressDialog();
        getDataManager().getAccessToken()
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
        getDataManager().getAccessToken()
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

    private void loadReviews(String id){
        getDataManager().loadReviews(id)
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
                        Log.d(LOG_TAG,"Total : "+reviewsResponse.getTotal());
                        for (Review review : reviewsResponse.getReviews()){
                            Log.d(LOG_TAG,review.toString());
                        }
                        getView().showReviews(reviewsResponse.getReviews());
                    }
                });
    }


    private void loadBusiness(String id) {
        getDataManager().loadBusinessDetails(id)
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
                        Log.d(LOG_TAG,e.getMessage());
                        getView().showOldBusiness();
                    }

                    @Override
                    public void onNext(Business business) {
                        Log.d(LOG_TAG,business.toString());
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
    public void makeCall(Business business) {
        if (business.getPhone() != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + business.getPhone()));
            if (intent.resolveActivity(getView().getViewActivity().getPackageManager()) != null) {
                getView().getViewActivity().startActivity(intent);
            }
        }
    }

    @Override
    public void goToWebsite(Business business) {
        if (business.getUrl() != null) {
            String url = business.getUrl();
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            getView().getViewActivity().startActivity(browserIntent);
        }

    }

    @Override
    public void addToFavourite(Business business) {

    }
}
