package com.example.pawel_piedel.thesis.main.tabs.cafes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.main.MainContract;
import com.example.pawel_piedel.thesis.main.tabs.BusinessAdapter;
import com.example.pawel_piedel.thesis.model.Business;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static dagger.internal.Preconditions.checkNotNull;

public class CafesFragment extends Fragment implements CafesContract.View {
    private static String LOG_TAG = CafesFragment.class.getName();
    private CafesContract.Presenter cafesPresenter;
    private BusinessAdapter businessAdapter = new BusinessAdapter();
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

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
        ButterKnife.bind(this, view);
        setUp(view);
        return view;
    }

    protected void setUp(View view) {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(businessAdapter);

        cafesPresenter.onViewPreapred();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setPresenter(@NonNull CafesContract.Presenter deliveriesPresenter) {
        cafesPresenter = checkNotNull(deliveriesPresenter);
    }

    @Override
    public void showCafes(List<Business> cafes) {
        businessAdapter.addBusinessList(cafes);
    }



}
