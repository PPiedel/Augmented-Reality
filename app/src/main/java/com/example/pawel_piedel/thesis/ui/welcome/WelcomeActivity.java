package com.example.pawel_piedel.thesis.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.ui.base.BaseActivity;
import com.example.pawel_piedel.thesis.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

/*Based on https://www.androidhive.info/2016/05/android-build-intro-slider-app/*/
public class WelcomeActivity extends BaseActivity implements WelcomeContract.View {

    @Inject
    WelcomePresenter<WelcomeContract.View> presenter;

    @BindView(R.id.welcome_screen_pager)
    ViewPager viewPager;

    @BindView(R.id.layoutDots)
    LinearLayout dotsLayout;

    @BindView(R.id.btn_next)
    Button nextButton;

    @BindView(R.id.btn_skip)
    Button skipButton;

    private TextView[] dots;
    private int[] layouts;

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            presenter.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        presenter.onViewDuringCreation();

    }

    public void initLayout() {
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        layouts = new int[]{
                R.layout.welcome_screen1,
                R.layout.welcome_screen_2,
                R.layout.welcome_screen_3
        };

        addBottomDots(0);

        setTransparentStatusBarColor();

        viewPager.setAdapter(new MyViewPagerAdapter());
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void moveToNextScreen() {
        viewPager.setCurrentItem(getItem()+1);
    }

    @Override
    public void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    @Override
    public void setSkipButtonVisibility(int visibility) {
        skipButton.setVisibility(visibility);
    }

    @Override
    public void setNextButtonText(String text) {
        nextButton.setText(text);
    }

    private void setTransparentStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void startMainActivity() {
        Intent mainActivityIntent = new Intent(getApplication(), MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    @OnClick(R.id.btn_skip)
    public void onSkipButtonClicked(){
        presenter.onSkipButtonClicked();
    }

    @OnClick(R.id.btn_next)
    public void onNextButtonClicked(){
        presenter.onNextButtonClicked(getItem());
    }

    private int getItem() {
        return viewPager.getCurrentItem();
    }

    @Override
    public int getLayoutsLength() {
        return layouts.length;
    }


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
