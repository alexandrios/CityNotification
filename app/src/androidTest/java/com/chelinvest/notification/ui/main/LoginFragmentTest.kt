package com.chelinvest.notification.ui.main

import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.EspressoIdlingResource
import com.chelinvest.notification.model.ObjAny
import org.hamcrest.Matchers
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.awaitility.Awaitility.await
import org.junit.*
import org.mockito.Mockito
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
    }

    @Test
    fun FirstTestMockito() {

/*
        // Mockito cannot mock/spy because :  - final class
        val mocktivity = Mockito.mock(activityActivityTestRule.activity::class.java)
        Log.wtf("TEST", activityActivityTestRule.activity.packageName)
        Mockito.`when`(mocktivity.packageName).thenReturn("world.must.live.in.piece")
        Log.wtf("TEST", mocktivity.packageName)
*/

        val o = ObjAny()
        o.id = "1"
        o.name = "Ping"
        Log.wtf("TEST", o.toString())
        Assert.assertEquals("id=1; name=Ping", o.toString())

        val mockObjAny = Mockito.mock(ObjAny::class.java)
        mockObjAny.id = "2"
        mockObjAny.name = "Courses"
        Log.wtf("TEST", mockObjAny.toString())
        `when`(mockObjAny.toString()).thenReturn("Something else")
        Log.wtf("TEST", mockObjAny.toString())

/*
        // Mockito cannot mock/spy because :  - final class
        val preferences = mock(Preferences::class.java)
        `when`(preferences.getAgentName(activityActivityTestRule.activity)).thenReturn("Superagent")
        Assert.assertEquals("Superagent",
            preferences.getAgentName(activityActivityTestRule.activity))
*/

    }
}