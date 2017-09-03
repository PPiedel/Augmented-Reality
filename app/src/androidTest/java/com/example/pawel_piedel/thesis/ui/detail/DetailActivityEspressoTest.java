package com.example.pawel_piedel.thesis.ui.detail;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.pawel_piedel.thesis.R;
import com.example.pawel_piedel.thesis.data.model.Business;
import com.example.pawel_piedel.thesis.data.model.Location;
import com.example.pawel_piedel.thesis.ui.main.BusinessAdapter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Pawel_Piedel on 03.09.2017.
 */
@RunWith(AndroidJUnit4.class)

public class DetailActivityEspressoTest {

    @Rule
    public ActivityTestRule<DetailActivity> mActivityRule = new ActivityTestRule<DetailActivity>(
            DetailActivity.class) {

        @Override
        protected Intent getActivityIntent() {
            Business business = new Business();
            business.setName("test");
            Location location = new Location();
            location.setAddress1("test");
            location.setAddress2("test");
            location.setZipCode("test");
            location.setCity("test");
            business.setLocation(location);
            business.setPhone("test");
            business.setId("test");

            Intent intent = new Intent();
            intent.putExtra(BusinessAdapter.BUSINESS, business);
            return intent;
        }
    };




    @Test
    public void onCallButtonClicked() throws Exception {
        onView(withId(R.id.call_action)).perform(click());

        intended(toPackage("com.android.phone"));
    }

}