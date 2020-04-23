package com.chelinvest.notification.ui.main

import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AddressFragmentTest {

    @Rule
    @JvmField
    var activityActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)

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

        /*
        var addressRecyclerView: RecyclerView?
        var itemCount: Int = 0
        scenario.onFragment {
            addressRecyclerView = it.view?.findViewById(R.id.addressRecyclerView)
            addressRecyclerView?.let { rv: RecyclerView ->
                //Log.wtf("ADDRESS_FRAGMENT_TEST", "childCount = ${rv.childCount}")
                val addressAdapter = rv.adapter
                if (addressAdapter != null) {
                    itemCount = (addressAdapter as AddressAdapter).groupCount ?: -1
                    Log.wtf("ADDRESS_FRAGMENT_TEST", "groupCount = $itemCount")
                }
            }
        }
*/
        Thread.sleep(5000)

        onView(withId(R.id.addressRecyclerView)).check(matches(ViewMatchers.isDisplayed()))

        // Свернуть первую группу списка
        onView(withId(R.id.addressRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()))
        Thread.sleep(500)
        // Развернуть первую группу списка
        onView(withId(R.id.addressRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()))
        Thread.sleep(500)
        // Потянуть вниз - для обновления
        onView(withId(R.id.addressRecyclerView)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                swipeDown()))
        Thread.sleep(500)

        //scenario.moveToState(Lifecycle.State.DESTROYED)

        activityActivityTestRule.run {
            var view: IView? = null
            launchFragmentInContainer<SubscrFragment>(null, themeResId = R.style.AppTheme)
                .onFragment {
                    view = it
                }
                val bundle =
                    AddressFragment.getBundleArguments("185", "Noch eine Teste eins, zwei, drei")
            launchFragment<NavHostFragment>().onFragment {
                //it.findNavController()  //view as CustomFragment<*>
                //    .navigate(R.id.action_subscrFragment_to_addressFragment, bundle)
            }
        }
        Thread.sleep(1000)
        //onView(withId(R.id.text)).check(matches(withText("Hello World!")))
    }

}

