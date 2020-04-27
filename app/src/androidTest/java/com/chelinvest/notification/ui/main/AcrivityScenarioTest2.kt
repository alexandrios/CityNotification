package com.chelinvest.notification.ui.main

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

// https://medium.com/stepstone-tech/better-tests-with-androidxs-activityscenario-in-kotlin-part-1-6a6376b713ea

@RunWith (AndroidJUnit4ClassRunner::class)
class AcrivityScenarioTest2 {

    lateinit var scenario: ActivityScenario<MainActivity>

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun myTest() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
            .putExtra("title", "Testing rules!")
        scenario = launchActivity(intent)
        // Your test code goes here.
        Thread.sleep(1000)
    }

    @Test
    fun myTestWithDifferentExtra() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
            .putExtra("title", "Something different")
        scenario = launchActivity(intent)
        // Your test code goes here.
        Thread.sleep(1000)
    }
}