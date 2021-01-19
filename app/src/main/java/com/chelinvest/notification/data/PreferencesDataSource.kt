package com.chelinvest.notification.data

import android.content.SharedPreferences
import com.chelinvest.notification.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesDataSource @Inject constructor(
    private val preferences: SharedPreferences
) {

    fun getInt(key: String?, defValue: Int) = preferences.getInt(key, defValue)
    private fun putInt(key: String?, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }
    private fun getString(key: String?): String? = preferences.getString(key, null)
    private fun getString(key: String?, defValue: String) = preferences.getString(key, defValue)
    private fun putString(key: String?, value: String?) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    private fun getBoolean(key: String?, defValue: Boolean) = preferences.getBoolean(key, defValue)
    private fun putBoolean(key: String?, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getSessionId() = getString(Constants.SESSION_ID, "")
    fun setSessionId(value: String) = putString(Constants.SESSION_ID, value)

    fun getLaunchCount() = getInt(Constants.LAUNCH_COUNT, 0)
    fun setLaunchCount(value: Int) = putInt(Constants.LAUNCH_COUNT, value)

    fun getBranchShort() = getString(Constants.BRANCH_SHORT, "")
    fun setBranchShort(value: String) = putString(Constants.BRANCH_SHORT, value)

    fun getFCMToken() = getString(Constants.FCM_TOKEN, "")
    fun setFCMToken(value: String?) = putString(Constants.FCM_TOKEN, value)

}