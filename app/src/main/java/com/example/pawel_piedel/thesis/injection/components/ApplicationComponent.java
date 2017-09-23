package com.example.pawel_piedel.thesis.injection.components;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.pawel_piedel.thesis.ThesisApplication;
import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.local.SharedPreferencesManager;
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

    DataManager dataManager();
}
