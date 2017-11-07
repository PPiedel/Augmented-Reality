package com.example.pawel_piedel.thesis.ui.main;

import com.example.pawel_piedel.thesis.data.BusinessRepository;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Pawel_Piedel on 30.08.2017.
 */
@RunWith(RobolectricTestRunner.class)
public class MainPresenterTest {

    @Mock
    MainContract.View mainView;

    @Mock
    BusinessRepository businessRepository;

    private MainPresenter<MainContract.View> mainPresenter;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        mainPresenter = new MainPresenter<>(businessRepository);
        mainPresenter.attachView(mainView);

    }


}