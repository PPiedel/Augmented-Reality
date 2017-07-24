package com.example.pawel_piedel.thesis.ui.augumented_reality;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.injection.ConfigPersistent;
import com.example.pawel_piedel.thesis.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Pawel_Piedel on 24.07.2017.
 */
@ConfigPersistent
public class ARPresenter<V extends ARContract.View> extends BasePresenter<V>  implements ARContract.Presenter<V> {

    @Inject
    public ARPresenter(DataManager dataManager){
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
