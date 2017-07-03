package com.example.pawel_piedel.thesis.tabs.coffes;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pawel_piedel.thesis.tabs.deliveries.DeliveriesFragment;
import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.tabs.restaurants.RestaurantsFragment;
import com.example.pawel_piedel.thesis.adapters.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        RestaurantsFragment.OnFragmentInteractionListener,
        CoffiesFragment.OnFragmentInteractionListener,
        DeliveriesFragment.OnFragmentInteractionListener {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String CLIENT_ID = "VokcbDNJly63jzOhJqJ0JA";
    public static final String CLIENT_SECRET = "gaFo3VLh1cNWS5L7nHJ6nRxVq97iRJCqvBAWnvmoiAWCf2xriOKhp6h5U0LNuj8F";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpLayout();


    }

    private void setUpLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupViewPager(viewPager);
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
