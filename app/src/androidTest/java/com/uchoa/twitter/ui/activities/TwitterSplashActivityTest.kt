package com.uchoa.twitter.ui.activities

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uchoa.twitter.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TwitterSplashActivityTest {

    private val timeout = 5000L
    private val conditionCHeckInterval = 100L

    @JvmField
    @Rule
    var mActivityRule: IntentsTestRule<TwitterSplashActivity> =
        IntentsTestRule(TwitterSplashActivity::class.java)

    @Test
    fun test_twitterActivityIsCalledOnAfterSplash() {
        onView(ViewMatchers.withId(R.id.start_image_logo)).check(matches(isDisplayed()))
        waitUntilActivityVisible<TwitterAppActivity>()

        intended(hasComponent(TwitterAppActivity::class.java.name))
    }

    private inline fun <reified T : Activity> waitUntilActivityVisible() {
        val startTime = System.currentTimeMillis()
        while (!isVisible<T>()) {
            Thread.sleep(conditionCHeckInterval)
            if (System.currentTimeMillis() - startTime >= timeout) {
                throw AssertionError("Activity ${T::class.java.simpleName} not visible after $timeout milliseconds")
            }
        }
    }

    private inline fun <reified T : Activity> isVisible(): Boolean {
        val am = ApplicationProvider.getApplicationContext<Context>()
            .getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val visibleActivityName = am.appTasks[0].taskInfo.baseActivity.className
        return visibleActivityName == T::class.java.name
    }
}