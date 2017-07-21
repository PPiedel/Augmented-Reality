package com.example.pawel_piedel.thesis.injection.modules;

import android.app.Application;
import android.content.Context;

import com.example.pawel_piedel.thesis.data.ApiService;
import com.example.pawel_piedel.thesis.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.example.pawel_piedel.thesis.data.ServiceFactory.createService;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    ApiService provideApiService() {
        return createService(ApiService.class);
    }
}
