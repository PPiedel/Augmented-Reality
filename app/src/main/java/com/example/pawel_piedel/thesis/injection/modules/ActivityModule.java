package com.example.pawel_piedel.thesis.injection.modules;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.pawel_piedel.thesis.injection.ActivityContext;
import com.example.pawel_piedel.thesis.injection.PerActivity;
import com.example.pawel_piedel.thesis.ui.MainContract;
import com.example.pawel_piedel.thesis.ui.MainPresenter;

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
    MainContract.Presenter<MainContract.View> provideMainPresenter(MainPresenter<MainContract.View> presenter){
        return presenter;
    }
}