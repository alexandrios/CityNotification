package com.chelinvest.notification.ui.main

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chelinvest.notification.R
import com.chelinvest.notification.ui.custom.stylable.CustomEditText
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ExampleInstrumentedTest {


    //@get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()
    @Rule
    @JvmField
    var activityActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    /*
        @Before
        fun setup() {
            launchFragmentInContainer<LoginFragment>(themeResId = R.style.AppTheme)
        }

     */


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.chelinvest.notification", appContext.packageName)
    }


    //@Test(timeout = 3000)
    @Test
    fun authentificationTestBase() {
        onView(withId(R.id.userEditText)).perform(click())
        onView(allOf(withId(R.id.vEditText), hasFocus())).perform(typeText("pam"), closeSoftKeyboard())
        onView(withId(R.id.passEditText)).perform(click())
        onView(allOf(withId(R.id.vEditText), hasFocus())).perform(typeText("ceramica1"), closeSoftKeyboard())
        onView(withId(R.id.viewPassImageView)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.branchRecyclerView)).check(matches(isDisplayed()))
        pressBack()
    }

    @Test
    fun authentificationTestBase_variant2() {
        val v = onView(allOf(withId(R.id.vEditText), instanceOf(CustomEditText::class.java),
            /*withParent(withId(R.id.vmetLinearLayout)),*/ modifiedEditTextMatcher(
                R.id.userEditText) ))
        v.perform(typeText("pam"))
        onView(allOf(withId(R.id.vEditText), instanceOf(CustomEditText::class.java),
            /*withParent(withId(R.id.vmetLinearLayout)),*/ modifiedEditTextMatcher(
                R.id.passEditText) ))
            .perform(typeText("ceramica1"))
        onView(withId(R.id.loginButton)).perform(click())
        Thread.sleep(5000)
        pressBack()
    }

    fun modifiedEditTextMatcher(idParent: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View?): Boolean {
                /*
                val linearLayout = item?.parent
                val parentView = linearLayout?.parent
                val id = (parentView as View).id
                val result = id == idParent
                return result
                */
                return (item?.parent?.parent as View).id == idParent
            }

            override fun describeTo(description: org.hamcrest.Description?) {
                description?.appendText("expected: ");
                description?.appendText("" + idParent)
            }
        }
    }

}
