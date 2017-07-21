package com.example.pawel_piedel.thesis.injection.components;

import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.injection.modules.ActivityModule;

import dagger.Component;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */

@ConfigPersistent
@Component(dependencies = ApplicationComponent.class)
public interface ConfigPersistentComponent {
    ActivityComponent activityComponent(ActivityModule activityModule);
}


