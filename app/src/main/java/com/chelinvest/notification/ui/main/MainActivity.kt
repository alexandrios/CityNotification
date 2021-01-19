package com.chelinvest.notification.ui.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.chelinvest.notification.R
import com.chelinvest.notification.di.injectActivityViewModel
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
//class MainActivity : CustomActivity<MainPresenter>() {

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @JvmField
    var mFirebaseAnalytics: FirebaseAnalytics? = null
    @Inject
    @JvmField
    var mInAppMessaging: FirebaseInAppMessaging? = null

    //override fun createPresenter(): MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "MainActivity -> onCreate")

        viewModel = injectActivityViewModel(viewModelFactory)

        //val sId = Preferences.getInstance().getSessionId(this)
        //Log.d(LOG_TAG, "MainActivity sessionId=$sId")

        setContentView(R.layout.activity_main)

        // Инициализация Firebase
        // https://console.firebase.google.com/project/city-agent-notification/settings/general/android:com.chelinvest.notification
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
        //mInAppMessaging = FirebaseInAppMessaging.getInstance()
        if (mInAppMessaging != null) {
            mInAppMessaging!!.isAutomaticDataCollectionEnabled = true
            mInAppMessaging!!.setMessagesSuppressed(false)
        }

        // Получить токен устройства для FireBase
        //getPresenter().getFCMToken(this)
        viewModel.getFCMToken(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "MainActivity onResume")
    }

}
