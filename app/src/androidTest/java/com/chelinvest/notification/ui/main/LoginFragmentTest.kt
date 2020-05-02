package com.chelinvest.notification.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.fragment.app.testing.launchFragment
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.EspressoIdlingResource
import com.chelinvest.notification.ui.CustomFragment
import com.chelinvest.notification.ui.login.LoginFragment
import com.chelinvest.notification.ui.login.LoginPresenter
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.awaitility.Awaitility.await
import java.util.concurrent.TimeUnit


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
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        //MockitoAnnotations.initMocks(this)
    }

    @After
    fun stopActivity() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        activityActivityTestRule.finishActivity()
    }

    @Test
    fun loginTest()  {

        Espresso.onView(ViewMatchers.withId(R.id.userEditText)).perform(ViewActions.click())
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.vEditText),
            ViewMatchers.hasFocus()))
            .perform(ViewActions.replaceText("pam"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.passEditText)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.passEditText)).perform(ViewActions.click())
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.vEditText),
            ViewMatchers.hasFocus()))
            .perform(ViewActions.replaceText("ceramica1"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.viewPassImageView)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())

        await()
            .atMost(5, TimeUnit.SECONDS)
            .ignoreExceptions()
            .untilAsserted {
                Espresso.onView(ViewMatchers.withId(R.id.branchRecyclerView))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            }

        //Thread.sleep(5000)
        //Espresso.onView(ViewMatchers.withId(R.id.branchRecyclerView))
        //    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        //pressBack()
    }


}