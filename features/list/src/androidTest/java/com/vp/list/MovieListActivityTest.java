package com.vp.list;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MovieListActivityTest {

    @Rule
    public ActivityTestRule<MovieListActivity> activityActivityTestRule =
            new ActivityTestRule<>(MovieListActivity.class);

    @Test
    public void TestShowRecyclerView () {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()));
    }
}