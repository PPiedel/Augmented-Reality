package com.example.pawel_piedel.thesis.activities;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity implements
        RestaurantsFragment.OnFragmentInteractionListener,
        CoffiesFragment.OnFragmentInteractionListener,
        DeliveriesFragment.OnFragmentInteractionListener {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String CLIENT_ID = "VokcbDNJly63jzOhJqJ0JA";
    public static final String CLIENT_SECRET = "gaFo3VLh1cNWS5L7nHJ6nRxVq97iRJCqvBAWnvmoiAWCf2xriOKhp6h5U0LNuj8F";

    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* new Thread(new Runnable() {
            public void run() {
                try {
                    YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
                    YelpFusionApi yelpFusionApi = apiFactory.createAPI(CLIENT_ID, CLIENT_SECRET);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(RestaurantsFragment.newInstance(), "Restaurants");
        adapter.addFragment(CoffiesFragment.newInstance(), "Coffeehouses");
        adapter.addFragment(DeliveriesFragment.newInstance(), "Delivery");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlaceSelected(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
