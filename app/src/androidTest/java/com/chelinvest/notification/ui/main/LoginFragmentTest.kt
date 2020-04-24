package com.chelinvest.notification.ui.main

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chelinvest.notification.R
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class LoginFragmentTest {

    @Rule
    @JvmField
    var activityActivityTestRule =
        ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun startActivity() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
            .putExtra("title", "Testing rules!")
        activityActivityTestRule.launchActivity(intent)
    }

    @After
    fun stopActivity() {
        activityActivityTestRule.finishActivity()
    }

    @Test
    fun loginTest()  {

        Espresso.onView(ViewMatchers.withId(R.id.userEditText)).perform(ViewActions.click())
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.vEditText),
            ViewMatchers.hasFocus()))
            .perform(ViewActions.typeText("pam"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.passEditText)).perform(ViewActions.click())
        Thread.sleep(100)
        Espresso.onView(ViewMatchers.withId(R.id.passEditText)).perform(ViewActions.click())
        Thread.sleep(100)
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.vEditText),
            ViewMatchers.hasFocus()))
            .perform(ViewActions.typeText("ceramica1"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.viewPassImageView)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())
        Thread.sleep(5000)
        Espresso.onView(ViewMatchers.withId(R.id.branchRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}