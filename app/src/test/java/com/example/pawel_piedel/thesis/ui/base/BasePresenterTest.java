package com.example.pawel_piedel.thesis.ui.base;

import com.example.pawel_piedel.thesis.data.business.local.BusinessRepositoryImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Pawel_Piedel on 02.09.2017.
 */
public class BasePresenterTest {

    @Mock
    BusinessRepositoryImpl businessRepositoryImpl;

    @Mock
    BaseView baseView;

    private BasePresenter<BaseView> basePresenter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        basePresenter = new BasePresenter<>(businessRepositoryImpl);
    }

    @Test
    public void attachView() throws Exception {
        basePresenter.attachView(baseView);

        assertNotNull(basePresenter.getView());
    }

    @Test
    public void detachView() throws Exception {
        basePresenter.detachView();

        assertNull(basePresenter.getView());
    }

}