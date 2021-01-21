package com.chelinvest.notification.data

import android.content.SharedPreferences
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.BRANCH_SHORT
import com.chelinvest.notification.utils.Constants.CHANGE_ADDRESS
import com.chelinvest.notification.utils.Constants.CHANGE_SUBSCR_LIST
import com.chelinvest.notification.utils.Constants.FCM_TOKEN
import com.chelinvest.notification.utils.Constants.LAUNCH_COUNT
import com.chelinvest.notification.utils.Constants.SESSION_ID
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

    fun getSessionId() = getString(SESSION_ID, "")
    fun setSessionId(value: String) = putString(SESSION_ID, value)

    fun getLaunchCount() = getInt(LAUNCH_COUNT, 0)
    fun setLaunchCount(value: Int) = putInt(LAUNCH_COUNT, value)

    fun getBranchShort() = getString(BRANCH_SHORT, "")
    fun setBranchShort(value: String) = putString(BRANCH_SHORT, value)

    fun getFCMToken() = getString(FCM_TOKEN, "")
    fun setFCMToken(value: String?) = putString(FCM_TOKEN, value)

    fun getChangeSubscrList() = getBoolean(CHANGE_SUBSCR_LIST, false)
    fun setChangeSubscrList(value: Boolean) = putBoolean(CHANGE_SUBSCR_LIST, value)

    fun getChangeAddress() = getBoolean(CHANGE_ADDRESS, false)
    fun setChangeAddress(value: Boolean) = putBoolean(CHANGE_ADDRESS, value)

}