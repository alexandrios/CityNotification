package com.chelinvest.notification.data

import android.os.Build
import com.chelinvest.notification.data.remote.RemoteDataSource
import com.chelinvest.notification.model.session.Session
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
//    private val favouritesDao: FavouritesDao,
//    private val wordDao: WordDao,
//    private val rusWordDao: RusWordDao,
//    private val historyDao: HistoryDao,
//    private val changesDao: ChangesDao,
    private val remoteDataSource: RemoteDataSource
//    private val wordDataSourfce: WordDataSource,
//    private val preferencesDataSource: PreferencesDataSource
) {
    /**
     * Preferences
     */
//    fun getLanguage() = preferencesDataSource.getLanguage()
//    fun setLanguage(value: Boolean) = preferencesDataSource.setLanguage(value)
//
//    fun getTouchEvent() = preferencesDataSource.getTouchEvent()
//    fun setTouchEvent(value: Boolean) = preferencesDataSource.setTouchEvent(value)
//
//    // Настройка "Оффлайн-режим"
//    //fun setOfflineModeSetting(value: Boolean) = putBoolean(OFFLINE_MODE, value)
//    //fun getOfflineModeSetting() = getBoolean(OFFLINE_MODE, false)
//
//    // Настройка "Сербский алфавит: латиница"
//    fun setSerbianLatinSetting(value: Boolean) = preferencesDataSource.setSerbianLatinSetting(value)
//    fun getSerbianLatinSetting() = preferencesDataSource.getSerbianLatinSetting()
//
//    // Настройка "Выделять цветом ударение в русских словах"
//    fun setColorStressSetting(value: Boolean) = preferencesDataSource.setColorStressSetting(value)
//    fun getColorStressSetting() = preferencesDataSource.getColorStressSetting()
//
//    // Настройка цветовой схемы приложения
//    fun setDarkThemeSetting(value: Boolean) = preferencesDataSource.setDarkThemeSetting(value)
//    fun getDarkThemeSetting() = preferencesDataSource.getDarkThemeSetting()
//
//
    /**
     * Remote
     */
    fun getSession(user: String, pass: String): Call<Session> = remoteDataSource.getSession(user, pass)

//
//    fun getChanges(changeId: Int): Call<List<ChangeInfo>> = remoteDataSource.getChanges(changeId)
//
//    fun postMessage() {
//        val info = """
//            Версия SDK: ${Build.VERSION.SDK_INT}
//            Наименование версии ОС: ${Build.ID}
//            Устройство: ${Build.DEVICE}
//            Изготовитель: ${Build.MANUFACTURER}
//            Модель: ${Build.MODEL}
//            """.trimIndent()
//        remoteDataSource.postMessage("1", "Привет разработчикам!", "fake@ugur.com", info)
//    }

}