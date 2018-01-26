package com.example.pawel_piedel.thesis.ui.tabs.cafes;

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

public class CafesFragment extends BaseFragment implements CafesContract.View {
    private static String LOG_TAG = CafesFragment.class.getName();
    private final BusinessAdapter businessAdapter = new BusinessAdapter();

    @Inject
    CafesContract.Presenter<CafesContract.View> cafesPresenter;
    @Inject
    RxPermissions rxPermissions;
    @BindView(R.id.cafes_recycler_view)
    RecyclerView mRecyclerView;

    public CafesFragment() {
        // Required empty public constructor
    }

    public static CafesFragment newInstance() {
        return new CafesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafes, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            cafesPresenter.attachView(this);
        }

        setUpRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        cafesPresenter.managePermissions();
    }

    @Override
    public void onPause() {
        super.onPause();
        cafesPresenter.clearSubscriptions();
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
        cafesPresenter.detachView();
        super.onDestroyView();
    }


    @Override
    public void showCafes(List<Business> cafes) {
        if (cafes != null) {
            businessAdapter.setBusinessList(cafes);
        }

    }

    public RxPermissions getRxPermissions() {
        return rxPermissions;
    }

    @Override
    public void setRxPermissions(RxPermissions rxPermissions) {
        this.rxPermissions = rxPermissions;
    }

    public void setCafesPresenter(CafesContract.Presenter<CafesContract.View> cafesPresenter) {
        this.cafesPresenter = cafesPresenter;
    }

    @Override
    public BusinessAdapter getBusinessAdapter() {
        return businessAdapter;
    }
}
