package com.example.pawel_piedel.thesis.ui.tabs.cafes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by Pawel_Piedel on 02.09.2017.
 */
public class CafesPresenterTest {

    @Mock
    CafesPresenter<CafesContract.View> cafesPresenter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onViewPrepared() throws Exception {
        cafesPresenter.onViewPrepared();
    }

    @Test
    public void load() throws Exception {
    }

}