package com.chelinvest.notification.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.chelinvest.notification.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RecordedMainActivityTest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun recordedMainActivityTest2() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val customEditText = onView(allOf(withId(R.id.vEditText),
            childAtPosition(childAtPosition(withId(R.id.userEditText), 0), 1),
            isDisplayed()))
        customEditText.perform(replaceText("pam"), closeSoftKeyboard())

        val customEditText2 = onView(allOf(withId(R.id.vEditText),
            childAtPosition(childAtPosition(withId(R.id.passEditText), 0), 1),
            isDisplayed()))
        customEditText2.perform(replaceText("ceramic"), closeSoftKeyboard())

        val appCompatImageView = onView(allOf(withId(R.id.viewPassImageView),
            childAtPosition(childAtPosition(withId(R.id.vLoginLayout), 4), 0),
            isDisplayed()))
        appCompatImageView.perform(click())

        val customEditText3 = onView(allOf(withId(R.id.vEditText),
            withText("ceramic"),
            childAtPosition(childAtPosition(allOf(withId(R.id.passEditText), withText("ceramic")),
                0), 1),
            isDisplayed()))
        customEditText3.perform(replaceText("ceramica"))

        val customEditText4 = onView(allOf(withId(R.id.vEditText),
            withText("ceramica"),
            childAtPosition(childAtPosition(allOf(withId(R.id.passEditText), withText("ceramic")),
                0), 1),
            isDisplayed()))
        customEditText4.perform(closeSoftKeyboard())

        val customEditText5 = onView(allOf(withId(R.id.vEditText),
            withText("ceramica"),
            childAtPosition(childAtPosition(allOf(withId(R.id.passEditText), withText("ceramica")),
                0), 1),
            isDisplayed()))
        customEditText5.perform(click())

        val customEditText6 = onView(allOf(withId(R.id.vEditText),
            withText("ceramica"),
            childAtPosition(childAtPosition(allOf(withId(R.id.passEditText), withText("ceramica")),
                0), 1),
            isDisplayed()))
        customEditText6.perform(replaceText("ceramica1"))

        val customEditText7 = onView(allOf(withId(R.id.vEditText),
            withText("ceramica1"),
            childAtPosition(childAtPosition(allOf(withId(R.id.passEditText), withText("ceramica")),
                0), 1),
            isDisplayed()))
        customEditText7.perform(closeSoftKeyboard())

        val appCompatButton = onView(allOf(withId(R.id.loginButton),
            withText("Войти"),
            childAtPosition(allOf(withId(R.id.vLoginLayout),
                childAtPosition(withId(R.id.vParentLayout), 0)), 5),
            isDisplayed()))
        appCompatButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        val cardItem = onView(allOf(withId(R.id.cardItem),
            childAtPosition(childAtPosition(withId(R.id.branchRecyclerView), 0), 0),
            isDisplayed()))
        cardItem.perform(click())

        val appCompatImageView2 = onView(allOf(withId(R.id.vAddButton),
            childAtPosition(allOf(withId(R.id.vTitlePanel),
                childAtPosition(withId(R.id.vParentLayout), 1)), 2),
            isDisplayed()))
        appCompatImageView2.perform(click())

        val mDButton = onView(allOf(withId(R.id.md_buttonDefaultNegative),
            withText("Отмена"),
            childAtPosition(allOf(withId(R.id.md_root),
                childAtPosition(withId(android.R.id.content), 0)), 3),
            isDisplayed()))
        mDButton.perform(click())
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
}
