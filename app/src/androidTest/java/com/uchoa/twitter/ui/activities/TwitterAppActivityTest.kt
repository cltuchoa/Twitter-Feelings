package com.uchoa.twitter.ui.activities

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.uchoa.twitter.R
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TwitterAppActivityTest {

    @JvmField
    @Rule
    var mActivityRule: ActivityTestRule<TwitterAppActivity> =
        ActivityTestRule(TwitterAppActivity::class.java)

    @Test
    fun test_clearButtonSetEmptyStringToUserName() {
        onView(withId(R.id.home_user_name)).perform(typeText("user name"))
        onView(withId(R.id.home_btn_clear)).perform(click())

        onView(withId(R.id.home_user_name)).check(matches(withText("")))
    }

    @Test
    fun test_showUserNameRequiredMessage() {
        onView(withId(R.id.home_user_name)).perform(typeText(""))
        onView(withId(R.id.home_btn_show)).perform(click())

        onView(withId(R.id.home_message_error)).check(matches(isDisplayed()))
    }

    @Test
    fun test_loadingIsDisplayedWhenClickInShowButton() {
        onView(withId(R.id.home_user_name)).perform(typeText("user name"))
        onView(withId(R.id.home_user_name)).perform(closeSoftKeyboard())
        onView(withId(R.id.home_btn_show)).perform(click())

        onView(withId(R.id.app_progress)).check(matches(isDisplayed()))
    }

    @Test
    fun test_disableShowButtonWhenAppIsLoading() {
        onView(withId(R.id.home_btn_show)).check(matches(isEnabled()))

        onView(withId(R.id.home_user_name)).perform(typeText("user name"))
        onView(withId(R.id.home_user_name)).perform(closeSoftKeyboard())
        onView(withId(R.id.home_btn_show)).perform(click())

        onView(withId(R.id.app_progress)).check(matches(isDisplayed()))
        onView(withId(R.id.home_btn_show)).check(matches(not(isEnabled())))
    }

    @Test
    fun test_ListFragmentIsVisibleWhenFetchAValidUserName() {
        onView(withId(R.id.home_user_name)).perform(typeText("Maria"))
        onView(withId(R.id.home_user_name)).perform(closeSoftKeyboard())
        onView(withId(R.id.home_btn_show)).perform(click())
        Thread.sleep(10000)
        onView(withId(R.id.tweet_list_user_name)).check(matches(withText("Maria")))
    }
}