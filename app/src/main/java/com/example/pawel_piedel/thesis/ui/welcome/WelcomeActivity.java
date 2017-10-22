package com.example.pawel_piedel.thesis.ui.welcome;

import android.os.Bundle;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.ui.base.BaseActivity;

import javax.inject.Inject;

public class WelcomeActivity extends BaseActivity {

    @Inject
    WelcomePresenter<WelcomeContract.View> presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}
