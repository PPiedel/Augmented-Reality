package com.example.pawel_piedel.thesis;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

@Module
public final class ApplicationModule {

    private final Context mContext;

    ApplicationModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}