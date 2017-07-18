package com.example.pawel_piedel.thesis.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import static dagger.internal.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    MainContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPresenter(new MainPresenter(this));
        //mPresenter.start();

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
    public void setPresenter(@NonNull  MainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
