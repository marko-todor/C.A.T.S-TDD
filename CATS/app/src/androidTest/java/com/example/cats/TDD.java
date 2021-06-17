package com.example.cats;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.object.HasToString;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import static org.hamcrest.core.AnyOf.anyOf;

//for special meatcher
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TDD {

    @Rule
    public ActivityTestRule<MainActivity>
            mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.imagePlay)).perform(click());
        onView(isRoot()).perform(waitFor(5000));
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
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());

        onView(isRoot()).perform(waitFor(17000));

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

    @Test
    public void check_username_change_error_username_exists() {
        onView(withId(R.id.nickname)).perform(typeText("TestUserExists"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());

        onView(withId(R.id.logout)).perform(click());

        onView(withId(R.id.imagePlay)).perform(click());

        onView(isRoot()).perform(waitFor(5000));

        onView(withId(R.id.nickname)).perform(typeText("TestUsernameError"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());

        onView(withId(R.id.settings)).perform(click());
        onView(withText("Change Username")).perform(click());

        onView(withId(R.id.new_username_edit)).perform(typeText("TestUserExists"));
        onView(withId(R.id.new_username_edit)).perform(pressImeActionButton());
        onView(withId(R.id.button_save_new_username)).perform(click());

        onView(withText("Username TestUserExists is already taken")).check(matches(isDisplayed()));

    }

    @Test
    public void check_username_change_success() {
        onView(withId(R.id.nickname)).perform(typeText("TestUsernameChange"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(withId(R.id.settings)).perform(click());
        onView(withText("Change Username")).perform(click());

        onView(withId(R.id.new_username_edit)).perform(typeText("TestUserChangeNew"));
        onView(withId(R.id.new_username_edit)).perform(pressImeActionButton());
        onView(withId(R.id.button_save_new_username)).perform(click());

        onView(withId(R.id.btn_dialog)).perform(click());

        onView(withId(R.id.stats)).perform(click());
        onData(HasToString.hasToString(CoreMatchers.startsWith("TestUserChangeNew : 0")))
                .inAdapterView(withId(R.id.list_view_high_scores)).atPosition(0)
                .check(matches(isDisplayed()));

        onView(withId(R.id.btn_dialog)).perform(click());

        onView(withId(R.id.logout)).perform(click());

        onView(isRoot()).perform(waitFor(5000));

        onData(HasToString.hasToString(CoreMatchers.startsWith("TestUserChangeNew")))
                .inAdapterView(withId(R.id.list_view_users)).atPosition(0)
                .check(matches(isDisplayed()));

    }

    @Test
    public void check_delete_account_button() {
        onView(withId(R.id.nickname)).perform(typeText("TestDeleteAccountButton"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(withId(R.id.settings)).perform(click());
        onView(withText("DELETE ACCOUNT")).check(matches(isDisplayed()));
    }

    @Test
    public void check_delete_account_no() {
        onView(withId(R.id.nickname)).perform(typeText("TestDeleteAccountNo"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(withId(R.id.settings)).perform(click());
        onView(withText("DELETE ACCOUNT")).perform(click());
        onView(withText("Are you sure you want to delete this account?")).check(matches(isDisplayed()));
        onView(withText("NO")).perform(click());
        onView(withText("DELETE ACCOUNT")).check(matches(isDisplayed()));
    }

    @Test
    public void check_delete_account_yes() {
        onView(withId(R.id.nickname)).perform(typeText("TestDeleteAccountYes"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(withId(R.id.settings)).perform(click());
        onView(withText("DELETE ACCOUNT")).perform(click());
        onView(withText("Are you sure you want to delete this account?")).check(matches(isDisplayed()));
        onView(withText("YES")).perform(click());

        onView(isRoot()).perform(waitFor(5000));

        onView(withText("TestDeleteAccountYes")).check(doesNotExist());
    }

    @Test
    public void check_pause_icon_toggle(){
        onView(withId(R.id.nickname)).perform(typeText("TestPauseButtonToggle"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());
        onView(withId(R.id.imageView_pause)).check(matches(EspressoTestsMatchers.withDrawable(R.drawable.pause)));
        onView(withId(R.id.imageView_pause)).perform(click());
        onView(withId(R.id.imageView_pause)).check(matches(EspressoTestsMatchers.withDrawable(R.drawable.play2)));
    }

    @Test
    public void check_pause_stopping_time(){
        onView(withId(R.id.nickname)).perform(typeText("TestPauseStoppingTime"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());
        onView(withId(R.id.imageView_pause)).perform(click());
        String time1 = getText(withId(R.id.textView_timer));
        onView(isRoot()).perform(waitFor(2000));
        String time2 = getText(withId(R.id.textView_timer));
        assertEquals(time1, time2);
    }

    @Test
    public void check_pause_time_continue(){
        onView(withId(R.id.nickname)).perform(typeText("TestPauseTimeContinue"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());
        onView(withId(R.id.imageView_pause)).perform(click());
        String time1 = getText(withId(R.id.textView_timer));
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.imageView_pause)).perform(click());
        onView(isRoot()).perform(waitFor(2000));
        String time2 = getText(withId(R.id.textView_timer));
        assertNotEquals(time1, time2);
    }

    @Test
    public void check_pause_fight_stops(){
        onView(withId(R.id.nickname)).perform(typeText("TestPauseTimeContinue"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.imageView_pause)).perform(click());
        onView(isRoot()).perform(waitFor(15000));
        onView(withId(R.id.textView_timer)).check(matches(isDisplayed()));
    }

    @Test
    public void check_pause_fight_continues(){
        onView(withId(R.id.nickname)).perform(typeText("TestPauseTimeContinue"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.imageView_pause)).perform(click());
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.imageView_pause)).perform(click());
        onView(isRoot()).perform(waitFor(15000));
        onView(withId(R.id.button_go_to_garage)).check(matches(isDisplayed()));
    }

    @Test
    public void check_save_checkpoint_button_dissapearing_and_toast(){
        onView(withId(R.id.nickname)).perform(typeText("TestCheckpointButtonAndToast"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.saveAndExit)).check(matches(isDisplayed()));
        onView(withId(R.id.saveAndExit)).perform(click());
        onView(withText("Checkpoint saved!")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow()
                .getDecorView()))).check(matches(isDisplayed()));
        onView(withId(R.id.saveAndExit)).check(matches(not(isDisplayed())));
    }

    @Test
    public void check_save_checkpoint_restart_button_not_present(){
        onView(withId(R.id.nickname)).perform(typeText("TestCheckpointRestartButton1"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.restart)).check(matches(not(isDisplayed())));
    }

    @Test
    public void check_save_checkpoint_restart_button_present(){
        onView(withId(R.id.nickname)).perform(typeText("TestCheckpointRestartButton2"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.saveAndExit)).perform(click());
        onView(isRoot()).perform(waitFor(15000));
        onView(withId(R.id.button_go_to_garage)).perform(click());
        onView(withId(R.id.restart)).check(matches(isDisplayed()));
    }

    @Test
    public void check_save_checkpoint_time(){
        onView(withId(R.id.nickname)).perform(typeText("TestSaveCheckpointTime"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.saveAndExit)).perform(click());
        String time1 = getText(withId(R.id.textView_timer));
        onView(isRoot()).perform(waitFor(15000));
        onView(withId(R.id.button_go_to_garage)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.restart)).perform(click());
        String time2 = getText(withId(R.id.textView_timer));
        assertEquals(time1, time2);
    }

    @Test
    public void check_save_checkpoint_icon_disappear() {
        onView(withId(R.id.nickname)).perform(typeText("TestSaveIconDisappear"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_fight)).perform(click());
        onView(isRoot()).perform(waitFor(9000));
        onView(withId(R.id.saveAndExit)).check(matches(not(isDisplayed())));
    }

    @Test
    public void check_change_avatar_button() {
        onView(withId(R.id.nickname)).perform(typeText("TestChangeAvatarButton"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_change_avatar)).check(matches(isDisplayed()));
    }

    @Test
    public void check_change_avatar_avatars_not_present_until_clicked() {
        onView(withId(R.id.nickname)).perform(typeText("TestChangeAvatarNoAvatars"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        checkAvatarsDisappear();
    }

    public void checkAvatarsDisappear() {
        onView(withId(R.id.cat_my_gif1)).check(matches(not(isDisplayed())));
        onView(withId(R.id.cat_my_gif2)).check(matches(not(isDisplayed())));
        onView(withId(R.id.cat_my_gif3)).check(matches(not(isDisplayed())));
        onView(withId(R.id.cat_my_gif4)).check(matches(not(isDisplayed())));
    }

    @Test
    public void check_change_avatar_avatars_present_when_clicked() {
        onView(withId(R.id.nickname)).perform(typeText("TestChangeAvatarYesAvatars"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withId(R.id.cat_my_gif1)).check(matches(isDisplayed()));
        onView(withId(R.id.cat_my_gif2)).check(matches(isDisplayed()));
        onView(withId(R.id.cat_my_gif3)).check(matches(isDisplayed()));
        onView(withId(R.id.cat_my_gif4)).check(matches(isDisplayed()));
    }


    @Test
    public void check_change_avatar_avatars_disappear_when_clicked() {
        onView(withId(R.id.nickname)).perform(typeText("TestChangeAvatarAvatarsDisappear"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withId(R.id.cat_my_gif1)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        checkAvatarsDisappear();

        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withId(R.id.cat_my_gif2)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        checkAvatarsDisappear();

        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withId(R.id.cat_my_gif3)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        checkAvatarsDisappear();

        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withId(R.id.cat_my_gif4)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        checkAvatarsDisappear();

    }

    @Test
    public void check_change_avatar_avatars_message() {
        onView(withId(R.id.nickname)).perform(typeText("TestChangeAvatarAvatarsMessage"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withText("Choose your character")).check(matches(isDisplayed()));
    }


    @Test
    public void check_change_avatar_displayed_toast() {
        onView(withId(R.id.nickname)).perform(typeText("TestChangeAvatarAvatarsToast"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(withId(R.id.cat_my_gif1)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withText("Already chosen!")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow()
                .getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void check_change_avatar_garage() {
        onView(withId(R.id.nickname)).perform(typeText("TestChangeAvatarGarage"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));


        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.cat_my_gif1)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withText("Already chosen!")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow()
                .getDecorView()))).check(matches(isDisplayed()));

        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.cat_my_gif2)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.cat_my_gif2)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withText("Already chosen!")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow()
                .getDecorView()))).check(matches(isDisplayed()));

        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.cat_my_gif3)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.cat_my_gif3)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withText("Already chosen!")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow()
                .getDecorView()))).check(matches(isDisplayed()));


        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.cat_my_gif4)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withId(R.id.cat_my_gif4)).perform(click());
        onView(isRoot()).perform(waitFor(100));

        onView(withText("Already chosen!")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow()
                .getDecorView()))).check(matches(isDisplayed()));


    }


    @Test
    public void check_change_avatar_db() {
        onView(withId(R.id.nickname)).perform(typeText("TestChangeAvatarDatabase"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(withId(R.id.cat_my_gif2)).perform(click());
        onView(isRoot()).perform(waitFor(1000));

        onView(withId(R.id.logout)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withId(R.id.imagePlay)).perform(click());
        onView(isRoot()).perform(waitFor(5000));

        onView(withId(R.id.nickname)).perform(typeText("TestChangeAvatarDatabase"));
        onView(withId(R.id.nickname)).perform(pressImeActionButton());
        onView(withId(R.id.btn_dialog)).perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.imageView_change_avatar)).perform(click());
        onView(withId(R.id.cat_my_gif2)).perform(click());
        onView(isRoot()).perform(waitFor(100));
        onView(withText("Already chosen!")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow()
                .getDecorView()))).check(matches(isDisplayed()));
    }


    @After
    public void tearDown() throws Exception {

    }

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }
}

