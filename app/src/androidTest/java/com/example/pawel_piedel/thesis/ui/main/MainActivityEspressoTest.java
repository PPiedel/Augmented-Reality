package com.example.pawel_piedel.thesis.ui.main;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.ui.augumented_reality.ARActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Pawel_Piedel on 24.09.2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityEspressoTest {

    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule = new IntentsTestRule<>(MainActivity.class);


    @Test
    public void onFabClick(){
        onView(withId(R.id.fab)).perform(click());

        intended(hasComponent(ARActivity.class.getName()));
    }

    @Test
    public void swipePage() {
        onView(withId(R.id.viewpager))
                .check(matches(isDisplayed()));

        onView(withId(R.id.viewpager))
                .perform(swipeLeft());

        onView(withId(R.id.viewpager))
                .perform(swipeLeft());

        onView(withId(R.id.viewpager))
                .perform(swipeRight());

        onView(withId(R.id.viewpager))
                .perform(swipeRight());
    }




}