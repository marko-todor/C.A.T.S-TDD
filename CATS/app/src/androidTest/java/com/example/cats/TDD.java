package com.example.cats;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AnyOf.anyOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TDD {

    @Rule
    public ActivityTestRule<MainActivity>
            mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() throws Exception {
        try {
            onView(withId(R.id.imagePlay)).perform(click());
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void check_if_highscore_is_available() {
        onView(withId(R.id.nickname)).perform(typeText("TestHighScoreExists"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(withId(R.id.stats)).perform(click());
        onView(withText("High Score")).check(matches(isDisplayed()));
        onData(HasToString.hasToString(CoreMatchers.startsWith("TestHighScoreExists : 0")))
                .inAdapterView(withId(R.id.list_view_high_scores)).atPosition(0)
                .check(matches(isDisplayed()));
    }

    @Test
    public void check_if_highscore_works() {
        onView(withId(R.id.nickname)).perform(typeText("TestHighScoreWorks"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(withId(R.id.stats)).perform(click());
        onData(HasToString.hasToString(CoreMatchers.startsWith("TestHighScoreWorks : 0")))
                .inAdapterView(withId(R.id.list_view_high_scores)).atPosition(0)
                .check(matches(isDisplayed()));
        onView(withId(R.id.btn_dialog)).perform(click());

        onView(withId(R.id.imageView_fight)).perform(click());
        try {
            Thread.sleep(17000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.button_go_to_garage)).perform(click());
        onView(withId(R.id.stats)).perform(click());
        onData(HasToString.hasToString(CoreMatchers.startsWith("TestHighScoreWorks : 1")))
                .inAdapterView(withId(R.id.list_view_high_scores)).atPosition(0)
                .check(matches(isDisplayed()));
    }

    @Test
    public void check_username_change_button() {
        onView(withId(R.id.nickname)).perform(typeText("TestUsernameChangeButton"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(withId(R.id.settings)).perform(click());
        onView(withText("Change Username")).check(matches(isDisplayed()));
    }

    @Test
    public void check_username_change_dialog() {
        onView(withId(R.id.nickname)).perform(typeText("TestUserDialog"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(withId(R.id.settings)).perform(click());
        onView(withText("Change Username")).perform(click());
        onView(withText("Old username:")).check(matches(isDisplayed()));
        onView(withText("New username:")).check(matches(isDisplayed()));
        onView(withText("Save")).check(matches(isDisplayed()));
    }
    
    @After
    public void tearDown() throws Exception {

    }
}

