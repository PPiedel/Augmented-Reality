/*
  Copyright 2017 Google Inc. All Rights Reserved.
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

/*Permissions checking based on https://github.com/googlesamples/android-play-location*/

package com.example.pawel_piedel.thesis.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.adapters.ViewPagerAdapter;
import com.example.pawel_piedel.thesis.main.tabs.cafes.CafesFragment;
import com.example.pawel_piedel.thesis.main.tabs.cafes.CafesPresenter;
import com.example.pawel_piedel.thesis.main.tabs.deliveries.DeliveriesFragment;
import com.example.pawel_piedel.thesis.main.tabs.deliveries.DeliveriesPresenter;
import com.example.pawel_piedel.thesis.main.tabs.restaurants.RestaurantsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.pawel_piedel.thesis.util.Util.REQUEST_PERMISSIONS_REQUEST_CODE;
import static dagger.internal.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private MainContract.Presenter mPresenter;
    private SharedPreferences sharedPreferences;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        setPresenter(new MainPresenter(this));

        setUpLayout();

        setupViewPager(viewPager);

        setUpTabLayout();

        mPresenter.start();


    }



    private void setUpLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(RestaurantsFragment.newInstance(), "Restaurants");

        CafesFragment cafesFragment = CafesFragment.newInstance();
        CafesPresenter presenter = new CafesPresenter(cafesFragment,sharedPreferences);
        adapter.addFragment(cafesFragment, "Cafes");

        DeliveriesFragment deliveriesFragment = DeliveriesFragment.newInstance();
        DeliveriesPresenter presenter1 = new  DeliveriesPresenter(deliveriesFragment,sharedPreferences);
        adapter.addFragment(deliveriesFragment, "Delivery");

        viewPager.setAdapter(adapter);
    }

    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setPresenter(@NonNull  MainContract.Presenter deliveriesPresenter) {
        mPresenter = checkNotNull(deliveriesPresenter);
    }

    @Override
    public void showSnackbar(int mainTextStringId, int actionStringId, View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void showPermissionsRequest() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(LOG_TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(LOG_TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults){
        mPresenter.onPermissionResult(requestCode,permissions,grantResults);

    }

    @Override
    public Activity getViewActivity() {
        return this;
    }
}
