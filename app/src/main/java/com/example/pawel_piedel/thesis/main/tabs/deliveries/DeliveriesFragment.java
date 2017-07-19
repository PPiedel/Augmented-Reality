package com.example.pawel_piedel.thesis.main.tabs.deliveries;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.model.Business;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class DeliveriesFragment extends Fragment implements DeliveriesContract.View {
    private final String LOG_TAG = DeliveriesFragment.class.getName();
    private DeliveriesContract.Presenter deliveriesPresenter;

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
        return inflater.inflate(R.layout.fragment_deliveries, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deliveriesPresenter.start();
    }

    public void setPresenter(@NonNull DeliveriesContract.Presenter presenter) {
        deliveriesPresenter = checkNotNull(presenter);
    }


    @Override
    public void showDeliveries(List<Business> deliveries) {
        for (Business b : deliveries){
            Log.v(LOG_TAG,b.toString());
        }
    }



}
