package com.example.pawel_piedel.thesis.ui.base;

import com.example.pawel_piedel.thesis.data.DataManager;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */

public class BasePresenter<V extends BaseView> implements Presenter<V> {
    private DataManager dataManager;
    private V view;

    public BasePresenter() {
    }

    @Inject
    public BasePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public V getView() {
        return view;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

}
