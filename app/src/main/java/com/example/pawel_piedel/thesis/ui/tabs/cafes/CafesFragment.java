package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.pawel_piedel.thesis.injection.components.ActivityComponent;
import com.example.pawel_piedel.thesis.ui.base.BaseFragment;
import com.example.pawel_piedel.thesis.ui.base.Presenter;
import com.example.pawel_piedel.thesis.ui.tabs.restaurants.RestaurantsContract;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static dagger.internal.Preconditions.checkNotNull;

public class CafesFragment extends BaseFragment implements CafesContract.View {
    private String LOG_TAG = CafesFragment.class.getName();

    private BusinessAdapter businessAdapter = new BusinessAdapter();

    @Inject
    CafesContract.Presenter<CafesContract.View> cafesPresenter;

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
            //mBlogAdapter.setCallback(this);
        }

        setUpRecyclerView();
        return view;
    }

    protected void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(businessAdapter);

        cafesPresenter.onViewPrepared();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        cafesPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void requestRequiredPermissions(String[] permissions, int requestCode) {

    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public void showCafes(List<Business> cafes) {
        businessAdapter.addBusinessList(cafes);
    }

    @Override
    public Context provideContext() {
        return getActivity();
    }


}
