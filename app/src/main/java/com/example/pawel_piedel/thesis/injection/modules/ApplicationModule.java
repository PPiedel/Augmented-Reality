package com.example.pawel_piedel.thesis.injection.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.pawel_piedel.thesis.data.BusinessDataSource;
import com.example.pawel_piedel.thesis.data.BusinessRepository;
import com.example.pawel_piedel.thesis.data.local.LocalDataSource;
import com.example.pawel_piedel.thesis.data.local.SharedPreferencesManager;
import com.example.pawel_piedel.thesis.data.remote.ApiService;
import com.example.pawel_piedel.thesis.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.example.pawel_piedel.thesis.data.remote.ServiceFactory.createService;

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
    @Singleton
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

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return mApplication.getSharedPreferences("thesis_preferences", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    LocalDataSource provideLocalDataSource(SharedPreferences sharedPreferences) {
        return new SharedPreferencesManager(sharedPreferences);
    }

    @Provides
    @Singleton
    BusinessDataSource provideBusinessDataSource(@ApplicationContext Context context, ApiService apiService, LocalDataSource localDataSource) {
        return new BusinessRepository(context, apiService, localDataSource);
    }


}
