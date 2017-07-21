package com.example.pawel_piedel.thesis;

import android.app.Application;
import android.content.Context;

import com.example.pawel_piedel.thesis.injection.components.ApplicationComponent;
import com.example.pawel_piedel.thesis.injection.components.DaggerApplicationComponent;
import com.example.pawel_piedel.thesis.injection.modules.ApplicationModule;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

public class ThesisApplication extends Application {
    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static ThesisApplication get(Context context) {
        return (ThesisApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }
}
