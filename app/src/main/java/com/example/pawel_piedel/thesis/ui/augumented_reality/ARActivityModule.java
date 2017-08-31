package com.example.pawel_piedel.thesis.ui.augumented_reality;

import android.app.Activity;

import com.example.pawel_piedel.thesis.injection.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Pawel_Piedel on 31.08.2017.
 */

@Module
public abstract class ARActivityModule {
    @Provides
    @PerActivity
    static Activity provideActivity(ARActivity activity) {
        return activity;
    }
}
