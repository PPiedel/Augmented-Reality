package com.example.pawel_piedel.thesis.injection.components;

import com.example.pawel_piedel.thesis.injection.PerActivity;
import com.example.pawel_piedel.thesis.injection.modules.ActivityModule;
import com.example.pawel_piedel.thesis.ui.MainActivity;
import com.example.pawel_piedel.thesis.ui.tabs.cafes.CafesFragment;
import com.example.pawel_piedel.thesis.ui.tabs.deliveries.DeliveriesFragment;
import com.example.pawel_piedel.thesis.ui.tabs.restaurants.RestaurantsFragment;

import dagger.Component;
import dagger.Subcomponent;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(CafesFragment cafesFragment);
    void inject(DeliveriesFragment deliveriesFragment);
    void inject(RestaurantsFragment restaurantsFragment);
}
