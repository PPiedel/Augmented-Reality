package com.example.pawel_piedel.thesis.injection.components;

import android.content.Context;

import com.example.pawel_piedel.thesis.injection.ActivityContext;
import com.example.pawel_piedel.thesis.injection.PerActivity;
import com.example.pawel_piedel.thesis.injection.modules.ActivityModule;
import com.example.pawel_piedel.thesis.ui.augumented_reality.ARActivity;
import com.example.pawel_piedel.thesis.ui.detail.DetailActivity;
import com.example.pawel_piedel.thesis.ui.main.MainActivity;
import com.example.pawel_piedel.thesis.ui.main.tabs.cafes.CafesFragment;
import com.example.pawel_piedel.thesis.ui.main.tabs.deliveries.DeliveriesFragment;
import com.example.pawel_piedel.thesis.ui.main.tabs.restaurants.RestaurantsFragment;

import dagger.Subcomponent;

/**
 * Created by Pawel_Piedel on 20.07.2017.
 */

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(ARActivity arActivity);
    void inject(CafesFragment cafesFragment);
    void inject(DeliveriesFragment deliveriesFragment);
    void inject(RestaurantsFragment restaurantsFragment);
    void inject(DetailActivity detailActivity);

    @ActivityContext
    Context context();
}
