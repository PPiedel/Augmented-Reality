package com.example.pawel_piedel.thesis.injection.components;

import android.app.Application;
import android.content.Context;

import com.example.pawel_piedel.thesis.ThesisApplication;
import com.example.pawel_piedel.thesis.data.auth.AccessTokenRepository;
import com.example.pawel_piedel.thesis.data.business.BusinessRepository;
import com.example.pawel_piedel.thesis.data.location.LocationRepository;
import com.example.pawel_piedel.thesis.injection.ApplicationContext;
import com.example.pawel_piedel.thesis.injection.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(ThesisApplication application);

    @ApplicationContext
    Context context();

    Application application();

    BusinessRepository businessDataSource();

    LocationRepository locationRepository();

    AccessTokenRepository accessTokenRepository();



}
