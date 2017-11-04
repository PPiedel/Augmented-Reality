package com.example.pawel_piedel.thesis;

import android.app.Application;
import android.content.Context;

import com.example.pawel_piedel.thesis.data.BusinessRepository;
import com.example.pawel_piedel.thesis.injection.components.ApplicationComponent;
import com.example.pawel_piedel.thesis.injection.components.DaggerApplicationComponent;
import com.example.pawel_piedel.thesis.injection.modules.ApplicationModule;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

public class ThesisApplication extends Application {
    @Inject
    BusinessRepository businessRepository;
    private ApplicationComponent mApplicationComponent;

    public static ThesisApplication get(Context context) {
        return (ThesisApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }
}