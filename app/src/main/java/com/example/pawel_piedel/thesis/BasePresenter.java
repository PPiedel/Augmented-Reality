package com.example.pawel_piedel.thesis;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 21.07.2017.
 */

public class BasePresenter<V extends BaseView> implements Presenter<V> {
    private V view;

    @Inject
    public BasePresenter() {

    }

    @Override
    public void start() {

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
}
