package com.chelinvest.notification.ui.main

import android.os.Bundle
import android.util.Log
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.ui.CustomActivity

// Работа с Github
// https://metanit.com/java/android/22.1.php

// Введение в системы контроля версий
// http://all-ht.ru/inf/prog/p_0_0.html

// Путь до gitLab родительского проекта pushNot
// https://git.chelinvest.ru/aspn/mobile/android/app/pushnot

// Взаимодействие между фрагментами и активностью в Android Studio
// http://blog.harrix.org/article/7521

// Книга про git
// https://git-scm.com/book/ru/v2

// Share data between fragments
// https://developer.android.com/topic/libraries/architecture/viewmodel.html#sharing

class MainActivity : CustomActivity<MainPresenter>() {

    override fun createPresenter(): MainPresenter = MainPresenter()

    //var mFirebaseAnalytics: FirebaseAnalytics? = null
    //var mInAppMessaging: FirebaseInAppMessaging? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.wtf("MAINACTIVITY", "onCreate start")

        val sId = Preferences.getInstance().getSessionId(this)
        Log.wtf("session_id", "[MainActivity] session_id=" + sId)

        setContentView(R.layout.activity_main)

        /*
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mInAppMessaging = FirebaseInAppMessaging.getInstance()
        if (mInAppMessaging != null) {
            mInAppMessaging!!.isAutomaticDataCollectionEnabled = true
            mInAppMessaging!!.setMessagesSuppressed(false)
        }
        */

        Log.wtf("MAINACTIVITY", "onCreate end")
    }

    override fun onResume() {
        super.onResume()

        Log.wtf("MAINACTIVITY", "onResume")

        /*
        // Если уже была попытка авторизации
        if (Preferences.getInstance().getTryLogin(this)) {
            Preferences.getInstance().saveTryLogin(this, false)
            // и session_id тем не менее пуст
            if (Preferences.getInstance().getSessionId(this) == null) {
                // выйти из приложения
                finish()
            }
        */
/*
            val sId = Preferences.getInstance().getSessionId(this)
            Log.wtf("session_id", "[MainActivity] session_id=" + sId)

            // Увеличить счетчик успешных входов в приложение
            var launchCount = Preferences.getInstance().getLaunchCount(this)
            if (launchCount < 0)
                launchCount = 0
            Preferences.getInstance().saveLaunchCount(this, launchCount + 1)
*/
            // Получить токен устройства для FireBase
            //getPresenter().getFCMToken(this)
        //}
    }


}
