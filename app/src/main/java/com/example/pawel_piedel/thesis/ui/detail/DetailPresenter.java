package com.example.pawel_piedel.thesis.ui.detail;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 27.07.2017.
 */

@ConfigPersistent
public class DetailPresenter<V extends DetailContract.View> extends BasePresenter<V> implements DetailContract.Presenter<V> {
    private final static String LOG_TAG = DetailPresenter.class.getSimpleName();

    @Inject
    public DetailPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

}
