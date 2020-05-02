package com.chelinvest.notification.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.EspressoIdlingResource
import com.chelinvest.notification.ui.custom.stylable.CustomEditText
import junit.framework.Assert.assertEquals
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.awaitility.Awaitility.await
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

//@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ExampleInstrumentedTest {

    //@get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()
    //@Rule
    //val activityScenario = ActivityScenario.launch(MainActivity::class.java)

    @Rule
    @JvmField
    var activityActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)



    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }


    @Test
    fun test_isActivityInView() {
        onView(withId(R.id.nav_host_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.chelinvest.notification.stub", appContext.packageName)
        //assertEquals("com.chelinvest.notification", appContext.packageName)
    }


    //@Test(timeout = 3000)
    @Test
    fun authentificationTestBase() {

        onView(withId(R.id.userEditText)).perform(click())
        onView(allOf(withId(R.id.vEditText), hasFocus())).perform(typeText("pam"), closeSoftKeyboard())
        onView(withId(R.id.passEditText)).perform(click())
        onView(allOf(withId(R.id.vEditText), hasFocus())).perform(typeText("ceramica1"), closeSoftKeyboard())
        onView(withId(R.id.viewPassImageView)).perform(click())
        //Thread.sleep(1000)
        onView(withId(R.id.loginButton)).perform(click())
        //Thread.sleep(2000)
        await()
            .atMost(5, TimeUnit.SECONDS)
            .ignoreExceptions()
            .untilAsserted {
                onView(withId(R.id.branchRecyclerView)).check(matches(isDisplayed()))
            }

        // To get our recyclerVew
        val branchRecyclerView = activityActivityTestRule.activity.findViewById<RecyclerView>(R.id.branchRecyclerView)
        // to get itemCount
        val itemCount = branchRecyclerView.adapter?.itemCount ?: 0

        // Click at the last item (to launch LimitFragment)
        onView(withId(R.id.branchRecyclerView))
            //.inRoot(RootMatchers.withDecorView(Matchers.`is`(activityActivityTestRule.activity.window.decorView)))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(itemCount - 1, click()))

        // to check textViews of the fragment_limit
        onView(withId(R.id.agentTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.limitTextView)).check(matches(isDisplayed()))

        //Thread.sleep(2000)

        val vBack = onView(allOf(withId(R.id.vBackButton),
            childAtPosition(childAtPosition(withId(R.id.limitParent), 3), 0),
            isDisplayed()))
        vBack.perform(click())
        //Thread.sleep(2000)

        // Click at the first item
        onView(withId(R.id.branchRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        //Thread.sleep(2000)
        //pressBack()
    }

    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(
                    position)
            }
        }
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
        //pressBack()
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

    //val view = activityActivityTestRule.activity.findViewById(R.id.userEditText) as com.chelinvest.notification.ui.custom.ModifiedEditText
    //view.setText("pam")

    // Type text and then press the button.
    // -- onView(withId(R.id.userEditText)).perform(pressKey(65))
    //onView(withId(R.id.userEditText)).perform(typeText("pam"), closeSoftKeyboard())

    //onView(withId(R.id.changeTextBt)).perform(click())

    // Check that the text was changed.
    //onView(withId(R.id.textToBeChanged)).check(matches(withText(STRING_TO_BE_TYPED)))
}
