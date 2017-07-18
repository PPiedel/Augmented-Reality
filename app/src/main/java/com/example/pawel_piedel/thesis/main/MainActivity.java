package com.example.pawel_piedel.thesis.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.adapters.ViewPagerAdapter;
import com.example.pawel_piedel.thesis.main.tabs.cafes.CafesFragment;
import com.example.pawel_piedel.thesis.main.tabs.deliveries.DeliveriesFragment;
import com.example.pawel_piedel.thesis.main.tabs.restaurants.RestaurantsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpLayout();

        setupViewPager(viewPager);

        setUpTabLayout();


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
        adapter.addFragment(CafesFragment.newInstance(), "Cafes");
        adapter.addFragment(DeliveriesFragment.newInstance(), "Delivery");
        viewPager.setAdapter(adapter);
    }

    private void setUpTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
    }
}
