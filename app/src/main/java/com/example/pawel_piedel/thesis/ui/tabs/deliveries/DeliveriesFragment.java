package com.example.pawel_piedel.thesis.ui.tabs.deliveries;

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

public class DeliveriesFragment extends BaseFragment implements DeliveriesContract.View {
    private final String LOG_TAG = DeliveriesFragment.class.getName();

    private final BusinessAdapter businessAdapter = new BusinessAdapter();

    @Inject
    DeliveriesContract.Presenter<DeliveriesContract.View> deliveriesPresenter;

    @Inject
    RxPermissions rxPermissions;

    @BindView(R.id.deliveries_recycler_view)

    RecyclerView mRecyclerView;

    public DeliveriesFragment() {
        // Required empty public constructor
    }

    public static DeliveriesFragment newInstance() {
        return new DeliveriesFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_deliveries, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            deliveriesPresenter.attachView(this);
            //mBlogAdapter.setCallback(this);
        }

        setUpRecyclerView();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        deliveriesPresenter.managePermissions();
    }

    @Override
    public void onPause() {
        super.onPause();
        deliveriesPresenter.clearSubscriptions();
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
    public void onDestroyView() {
        deliveriesPresenter.detachView();
        super.onDestroyView();
    }


    @Override
    public void showDeliveries(List<Business> deliveries) {
        if (deliveries != null) {
            businessAdapter.setBusinessList(deliveries);
        }
    }

    @Override
    public Activity getParentActivity(){
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

    public void setDeliveriesPresenter(DeliveriesContract.Presenter<DeliveriesContract.View> deliveriesPresenter) {
        this.deliveriesPresenter = deliveriesPresenter;
    }

    public BusinessAdapter getBusinessAdapter() {
        return businessAdapter;
    }
}
