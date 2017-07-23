package com.example.pawel_piedel.thesis.ui.tabs.restaurants;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.adapters.BusinessAdapter;
import com.example.pawel_piedel.thesis.data.model.Business;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static dagger.internal.Preconditions.checkNotNull;

public class RestaurantsFragment extends Fragment implements RestaurantsContract.View {
    private final String LOG_TAG = RestaurantsFragment.class.getSimpleName();
    private RestaurantsContract.Presenter restaurantsPresenter;
    private BusinessAdapter businessAdapter = new BusinessAdapter();

    @BindView(R.id.restaurants_recycler_view)
    RecyclerView mRecyclerView;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    public static RestaurantsFragment newInstance(){
        return new RestaurantsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_restaurants, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        return view;
    }

    protected void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(businessAdapter);

        restaurantsPresenter.onViewPrepared();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        restaurantsPresenter.start();
    }

    @Override
    public void setPresenter(RestaurantsContract.Presenter restaurantsPresenter) {
        this.restaurantsPresenter = checkNotNull(restaurantsPresenter);
    }

    @Override
    public void requestPermissionsSafely(String[] permissions, int requestCode) {

    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void showRestaurants(List<Business> restaurants) {
        businessAdapter.addBusinessList(restaurants);
    }

    @Override
    public Context provideContext(){
        return getActivity();
    }




}
