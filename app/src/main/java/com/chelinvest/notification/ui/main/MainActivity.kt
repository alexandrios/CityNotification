package com.chelinvest.notification.ui.main

import android.os.Bundle
import android.util.Log
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.ui.CustomActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.inappmessaging.FirebaseInAppMessaging

class MainActivity : CustomActivity<MainPresenter>() {

    override fun createPresenter(): MainPresenter = MainPresenter()

    var mFirebaseAnalytics: FirebaseAnalytics? = null
    var mInAppMessaging: FirebaseInAppMessaging? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.wtf("MAINACTIVITY", "onCreate")

        val sId = Preferences.getInstance().getSessionId(this)
        Log.wtf("session_id", "[MainActivity] session_id=" + sId)

        setContentView(R.layout.activity_main)

        // Инициализация Firebase
        // https://console.firebase.google.com/project/city-agent-notification/settings/general/android:com.chelinvest.notification
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mInAppMessaging = FirebaseInAppMessaging.getInstance()
        if (mInAppMessaging != null) {
            mInAppMessaging!!.isAutomaticDataCollectionEnabled = true
            mInAppMessaging!!.setMessagesSuppressed(false)
        }

        // Получить токен устройства для FireBase
        getPresenter().getFCMToken(this)
    }

    override fun onResume() {
        super.onResume()
        Log.wtf("MAINACTIVITY", "onResume")
    }

}
