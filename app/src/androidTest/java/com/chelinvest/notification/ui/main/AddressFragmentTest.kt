package com.chelinvest.notification.ui.main

import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.SUBSCRIPTION_ID
import com.chelinvest.notification.additional.SUBSCRIPTION_NAME
import com.chelinvest.notification.ui.CustomFragment
import com.chelinvest.notification.ui.IView
import com.chelinvest.notification.ui.address.AddressFragment
import com.chelinvest.notification.ui.login.LoginFragment
import com.chelinvest.notification.ui.subscr.SubscrFragment
import org.awaitility.Awaitility
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
class AddressFragmentTest {

    @Rule
    @JvmField
    var activityActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

    private fun awaitMS(timeout: Long, assertion: (() -> Unit)) {
        Awaitility.await()
            .atMost(timeout, TimeUnit.MILLISECONDS)
            .ignoreExceptions()
            .untilAsserted(assertion)
    }

    @Test
    fun test_AddressFragment() {

        // The "fragmentArgs" and "factory" arguments are optional.
        val fragmentArgs = Bundle().apply {
            putString(SUBSCRIPTION_ID, "185")
            putString(SUBSCRIPTION_NAME, "Noch eine Teste eins, zwei, drei")
        }
        //val factory = FragmentFactory()
        val scenario =
            launchFragmentInContainer<AddressFragment>(fragmentArgs, themeResId = R.style.AppTheme)

        awaitMS(5000) {
            onView(withId(R.id.addressRecyclerView)).check(matches(ViewMatchers.isDisplayed()))
        }

        // Свернуть первую группу списка
        onView(withId(R.id.addressRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()))
        // Развернуть первую группу списка
        onView(withId(R.id.addressRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()))
        // Потянуть вниз - для обновления
        onView(withId(R.id.addressRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                swipeDown()))

        activityActivityTestRule.run {
            var view: IView? = null
            launchFragmentInContainer<SubscrFragment>(null,
                themeResId = R.style.AppTheme).onFragment {
                    view = it
                }
            val bundle =
                AddressFragment.getBundleArguments("185", "Noch eine Teste eins, zwei, drei")
            launchFragment<NavHostFragment>().onFragment {
                //it.findNavController()
                //    .navigate(R.id.action_subscrFragment_to_addressFragment, bundle)
                // it doesn't work: java.lang.RuntimeException: java.lang.IllegalStateException: no current navigation node
            }
        }

        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

}

