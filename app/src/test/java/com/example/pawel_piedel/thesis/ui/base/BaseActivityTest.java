package com.example.pawel_piedel.thesis.ui.base;

import android.app.AlertDialog;

import com.example.pawel_piedel.thesis.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Pawel_Piedel on 02.09.2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BaseActivityTest {

    BaseActivity baseActivity;


    @Before
    public void setUp() {
        baseActivity = Robolectric.setupActivity(BaseActivity.class);

        assertNotNull(baseActivity);

    }

    @Test
    public void showProgressDialog() throws Exception {
        baseActivity.showProgressDialog();

        assertNotNull(baseActivity.getProgressDialog());

        assertTrue(baseActivity.getProgressDialog().isShowing());
    }

    @Test
    public void hideProgressDialogWithoutShowingIt() throws Exception {
        baseActivity.hideProgressDialog();

        assertNull(baseActivity.getProgressDialog());
    }

    @Test
    public void hideProgressDialogAfterShowingIt() throws Exception {
        baseActivity.showProgressDialog();
        baseActivity.hideProgressDialog();

        assertFalse(baseActivity.getProgressDialog().isShowing());
    }

    @Test
    public void showAlert() throws Exception {
        baseActivity.showAlert("test", "test");
        AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();

        assertNotNull(alert);

        ShadowAlertDialog sAlert = shadowOf(alert);
        assertThat(sAlert.getTitle().toString(), is("test"));

    }


}