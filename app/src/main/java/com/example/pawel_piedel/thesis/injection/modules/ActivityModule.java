package com.example.pawel_piedel.thesis.injection.modules;

import android.app.Activity;
import android.content.Context;

import com.example.pawel_piedel.thesis.injection.ActivityContext;
import com.example.pawel_piedel.thesis.injection.PerActivity;
import com.example.pawel_piedel.thesis.ui.augumented_reality.ARContract;
import com.example.pawel_piedel.thesis.ui.augumented_reality.ARPresenter;
import com.example.pawel_piedel.thesis.ui.main.MainContract;
import com.example.pawel_piedel.thesis.ui.main.MainPresenter;
import com.example.pawel_piedel.thesis.ui.main.tabs.cafes.CafesContract;
import com.example.pawel_piedel.thesis.ui.main.tabs.cafes.CafesPresenter;
import com.example.pawel_piedel.thesis.ui.main.tabs.deliveries.DeliveriesContract;
import com.example.pawel_piedel.thesis.ui.main.tabs.deliveries.DeliveriesPresenter;
import com.example.pawel_piedel.thesis.ui.main.tabs.restaurants.RestaurantsContract;
import com.example.pawel_piedel.thesis.ui.main.tabs.restaurants.RestaurantsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context provideActivityContext() {
        return mActivity;
    }

    @Provides
    @PerActivity
    MainContract.Presenter<MainContract.View> provideMainPresenter(MainPresenter<MainContract.View> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    CafesContract.Presenter<CafesContract.View> provideCafesPresenter(CafesPresenter<CafesContract.View> presenter){
        return presenter;
    }

    @Provides
    @PerActivity
    DeliveriesContract.Presenter<DeliveriesContract.View> provideDeliveriesPresenter(DeliveriesPresenter<DeliveriesContract.View> presenter){
        return presenter;
    }

    @Provides
    @PerActivity
    RestaurantsContract.Presenter<RestaurantsContract.View> provideRestaurantsPresenter(RestaurantsPresenter<RestaurantsContract.View> presenter){
        return presenter;
    }

    @Provides
    @PerActivity
    ARContract.Presenter<ARContract.View> provideARPresenter(ARPresenter<ARContract.View> presenter){
        return presenter;
    }


}