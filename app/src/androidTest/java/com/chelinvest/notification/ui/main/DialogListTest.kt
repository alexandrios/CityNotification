package com.chelinvest.notification.ui.main

import android.view.KeyCharacterMap
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.EspressoKey
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.chelinvest.notification.R
import org.awaitility.Awaitility
import org.awaitility.Awaitility.await
import org.awaitility.Duration
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import javax.annotation.MatchesPattern

@RunWith(AndroidJUnit4ClassRunner::class)
class DialogListTest {

    @Rule
    @JvmField
    var activityActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    private fun awaitMS(timeout: Long, assertion: (() -> Unit)) {
        await()
            .atMost(timeout, TimeUnit.MILLISECONDS)
            .ignoreExceptions()
            .untilAsserted(assertion)
    }

    private fun stepsToSubscrFragment() {
        Espresso.onView(ViewMatchers.withId(R.id.userEditText)).perform(ViewActions.click())
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.vEditText),
            ViewMatchers.hasFocus()))
            .perform(ViewActions.replaceText("pam"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.passEditText)).perform(ViewActions.click())
        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.vEditText),
            ViewMatchers.hasFocus()))
            .perform(ViewActions.replaceText("ceramica1"), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click())
        //Thread.sleep(5000)

/*        await()
            .atMost(5, TimeUnit.SECONDS)
            .ignoreExceptions()
            .untilAsserted {
                onView(withId(R.id.branchRecyclerView)).check(matches(isDisplayed()))
            }*/

        awaitMS(5000) {
            onView(withId(R.id.branchRecyclerView)).check(matches(isDisplayed()))
        }

        //Espresso.onView(ViewMatchers.withId(R.id.branchRecyclerView))
        //    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Click at the first item
        onView(withId(R.id.branchRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()))

        //Thread.sleep(2000)
        //await().atMost(2000, TimeUnit.MILLISECONDS)
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
    fun launchDialogue() {

        stepsToSubscrFragment()

        var itemCount: Int? = null
        activityActivityTestRule.activity.findViewById<RecyclerView>(R.id.subscriptRecyclerView)?.let {
            itemCount = it.adapter?.itemCount
        }

        val subscriptRecyclerView = onView(withId(R.id.subscriptRecyclerView))
        subscriptRecyclerView.run {
            check(matches(isDisplayed()))
            // Скроллинг списка в конец
            perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>((itemCount ?: 1) - 1))
            //Thread.sleep(500)
            //awaitMS(500) {
                // Скроллинг списка в начало
                perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            //}
            //Thread.sleep(500)
            //awaitMS(500) {
                // Свайп влево
                perform(ViewActions.swipeLeft())
            //}
            //Thread.sleep(500)
            //awaitMS(500) {
                // Свайп вправо
                perform(ViewActions.swipeRight())
            //}
            //Thread.sleep(500)
        }

        // Показать только активные
        val isAct = onView(withText("Только активные подписки")).check(matches(isDisplayed()))
        isAct.perform(click())
        //Thread.sleep(500)
        //awaitMS(500) {
            // Вызвать диалог со списком
            val vAdd = Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.vAddButton),
                childAtPosition(Matchers.allOf(ViewMatchers.withId(R.id.vTitlePanel),
                    childAtPosition(ViewMatchers.withId(R.id.vParentLayout), 1)), 2),
                ViewMatchers.isDisplayed()))
            vAdd.perform(ViewActions.click())
        //}
        //Thread.sleep(500)

        // Скроллинг списка
        onView(withId(R.id.valuesRecyclerView)).check(matches(isDisplayed()))
        //there may not be such a position - 15
        onView(withId(R.id.valuesRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(15))

        //Thread.sleep(500)
        //awaitMS(500) {
            // Ввести фильтр 74
            onView(withId(R.id.searchEditText)).check(matches(isDisplayed()))
                .perform(typeText("74"), ViewActions.closeSoftKeyboard())
        //}

        //Thread.sleep(500)
        //awaitMS(500) {
            // Нажать кнопку "Отмена"
            val mCancel = onView(allOf(withId(R.id.md_buttonDefaultNegative),
                withText("Отмена"),
                childAtPosition(allOf(withId(R.id.md_root),
                    childAtPosition(withId(android.R.id.content), 0)), 3),
                isDisplayed()))
            mCancel.perform(click())
        //}

        //Thread.sleep(500)
        //awaitMS(500) {
            val vBack = Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.vBackButton),
                ViewMatchers.isDisplayed()))
            vBack.perform(ViewActions.click())
        //}
        //Thread.sleep(1000)

    }
}