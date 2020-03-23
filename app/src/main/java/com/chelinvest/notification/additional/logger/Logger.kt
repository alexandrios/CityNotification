package com.chelinvest.notification.additional.logger

import android.util.Log
import com.chelinvest.notification.BuildConfig

// http://developer.alexanderklimov.ru/android/debug/logcat.php
// https://habr.com/ru/post/420777/   -Логирование в Android Studio без кода

object Logger {
    val isDebug = false
    val TAG = "Logger"

    fun Log(statement: String) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, statement)
        }
    }

    fun Log(tag: String, statement: String) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, statement)
        }
    }
}
