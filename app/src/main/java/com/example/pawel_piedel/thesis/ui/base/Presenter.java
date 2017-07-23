package com.example.pawel_piedel.thesis.ui.base;

/**
 * Created by Pawel_Piedel on 18.07.2017.
 */

public interface Presenter<V extends BaseView> {

    void attachView(V view);

    void detachView();

}
