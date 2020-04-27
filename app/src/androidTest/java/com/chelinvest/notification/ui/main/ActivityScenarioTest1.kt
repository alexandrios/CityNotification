package com.chelinvest.notification.ui.main

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith

// https://medium.com/stepstone-tech/better-tests-with-androidxs-activityscenario-in-kotlin-part-1-6a6376b713ea

@RunWith (AndroidJUnit4ClassRunner::class)
class ActivityScenarioTest1 {


    @Test
    fun testMainActivityScenario() {

        val scenarioActivity = launchActivity<MainActivity>()
        scenarioActivity.moveToState(Lifecycle.State.RESUMED)
        scenarioActivity.onActivity { activity ->
            // do some stuff with the Activity
        }

        // or via intent:

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
            .putExtra("title", "Testing rules!")
        val scenarioIntent = launchActivity<MainActivity>(intent)
        scenarioIntent.onActivity { activity ->
            // do some stuff with the Activity
        }
    }

}