package com.example.pawel_piedel.thesis.main.tabs.cafes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.model.Business;

import java.util.List;

import javax.inject.Inject;

import static dagger.internal.Preconditions.checkNotNull;

public class CafesFragment extends Fragment implements CafesContract.View {

    private CafesContract.Presenter cafesPresenter;

    public CafesFragment() {
        setPresenter(new CafesPresenter(this));
    }

    public static CafesFragment newInstance() {

        return new CafesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coffies, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        cafesPresenter.start();

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(@NonNull CafesContract.Presenter presenter) {
        cafesPresenter = checkNotNull(presenter);
    }

    @Override
    public void showCafes(List<Business> cafes) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

}
