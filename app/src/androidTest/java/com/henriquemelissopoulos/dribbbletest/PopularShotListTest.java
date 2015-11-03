package com.henriquemelissopoulos.dribbbletest;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.henriquemelissopoulos.dribbbletest.controller.Config;
import com.henriquemelissopoulos.dribbbletest.view.activity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.os.SystemClock.sleep;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static android.support.test.espresso.Espresso.pressBack;


/**
 * Created by h on 03/11/15.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class PopularShotListTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        sleep(4000);
    }

    @Test
    public void checkShotsOpen() {
        for (int i = 0; i < Config.SHOTS_PER_PAGE + 1; i++) {
            onView(allOf(withId(R.id.rvShots), isDisplayed())).perform(RecyclerViewActions.scrollToPosition(i), RecyclerViewActions.actionOnItemAtPosition(i, click()));
            sleep(2000);
            pressBack();
            sleep(2000);
        }
    }

}
