package com.chelinvest.notification

import android.content.Context

open class BasePreferences {

    private val appPreferencesName = "Pushnot_preferences"

    protected fun saveInt(context: Context, key: String, value: Int) {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    protected fun getInt(context: Context, key: String): Int {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        return pref.getInt(key, -1)
    }

    protected fun getInt(context: Context, key: String, def: Int): Int {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        return pref.getInt(key, def)
    }

    protected fun saveLong(context: Context, key: String, value: Long) {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    protected fun getLong(context: Context, key: String): Long {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        return pref.getLong(key, -1)
    }

    protected fun saveString(context: Context, key: String, value: String?) {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    protected fun getString(context: Context, key: String): String? {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        return pref.getString(key, null)
    }

    protected fun saveBoolean(context: Context, key: String, value: Boolean) {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    protected fun getBoolean(context: Context, key: String, default: Boolean): Boolean {
        val pref = context.getSharedPreferences(appPreferencesName, Context.MODE_PRIVATE)
        return pref.getBoolean(key, default)
    }

    /*fun clear(context: Context) {
        val pref = context.getSharedPreferences(APP_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }*/

}