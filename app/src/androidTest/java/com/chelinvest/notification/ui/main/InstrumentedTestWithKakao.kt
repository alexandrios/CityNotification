package com.chelinvest.notification.ui.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.chelinvest.notification.R
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTestWithKakao {

    @Rule
    @JvmField
    var activityActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java)


    @Test
    fun changeText_sameActivity() {

        Thread.sleep(2000)

        val screen = TestActivityScreen()
        screen {
            vLoginLayout { isVisible() }

            userEditText {
                click()
                isVisible()
                Espresso.onView(Matchers.allOf(withId(R.id.vEditText), ViewMatchers.hasFocus()))
                    .perform(ViewActions.typeText("pam"))
            }

            passEditText {
                click()
                isVisible()
                Espresso.onView(Matchers.allOf(withId(R.id.vEditText), ViewMatchers.hasFocus()))
                    .perform(ViewActions.typeText("ceramica1"))

            }

            loginButton {
                isVisible()
                click()
            }

            Thread.sleep(2000)
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

}