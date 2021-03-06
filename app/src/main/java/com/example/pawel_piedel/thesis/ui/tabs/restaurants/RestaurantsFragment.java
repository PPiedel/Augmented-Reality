package com.example.pawel_piedel.thesis.ui.tabs.restaurants;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.injection.components.ActivityComponent;
import com.example.pawel_piedel.thesis.ui.base.BaseFragment;
import com.example.pawel_piedel.thesis.ui.main.BusinessAdapter;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantsFragment extends BaseFragment implements RestaurantsContract.View {
    private final String LOG_TAG = RestaurantsFragment.class.getSimpleName();

    private final BusinessAdapter businessAdapter = new BusinessAdapter();

    @Inject
    RestaurantsContract.Presenter<RestaurantsContract.View> restaurantsPresenter;

    @Inject
    RxPermissions rxPermissions;

    @BindView(R.id.restaurants_recycler_view)

    RecyclerView mRecyclerView;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    public static RestaurantsFragment newInstance() {
        return new RestaurantsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            restaurantsPresenter.attachView(this);
        }
        setUpRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        restaurantsPresenter.managePermissions();
    }

    @Override
    public void onPause() {
        super.onPause();
        restaurantsPresenter.clearSubscriptions();
    }

    private void setUpRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(businessAdapter);
        }
    }

    @Override
    public void showRestaurants(List<Business> list) {
        if (list != null) {
            businessAdapter.setBusinessList(list);
        }
    }

    @Override
    public Activity getParentActivity() {
        return getActivity();
    }

    @Override
    public RxPermissions getRxPermissions() {
        return rxPermissions;
    }

    @Override
    public void setRxPermissions(RxPermissions rxPermissions) {
        this.rxPermissions = rxPermissions;
    }

    @Override
    public BusinessAdapter getBusinessAdapter() {
        return businessAdapter;
    }

    public void setRestaurantsPresenter(RestaurantsContract.Presenter<RestaurantsContract.View> restaurantsPresenter) {
        this.restaurantsPresenter = restaurantsPresenter;
    }
}
