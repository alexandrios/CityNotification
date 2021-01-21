package com.chelinvest.notification.data

import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.api.response.MainDeliverySubscriptionResponse
import com.chelinvest.notification.data.remote.RemoteDataSource
import com.chelinvest.notification.model.ObjParam
import retrofit2.Call
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val preferencesDataSource: PreferencesDataSource
) {
    /**
     * Preferences
     */
    fun getSessionId() = preferencesDataSource.getSessionId()
    fun setSessionId(value: String) = preferencesDataSource.setSessionId(value)

    fun getLaunchCount() = preferencesDataSource.getLaunchCount()
    fun setLaunchCount(value: Int) = preferencesDataSource.setLaunchCount(value)

    fun getBranchShort() = preferencesDataSource.getBranchShort()
    fun setBranchShort(value: String) = preferencesDataSource.setBranchShort(value)

    fun getFCMToken() = preferencesDataSource.getFCMToken()
    fun setFCMToken(value: String?) = preferencesDataSource.setFCMToken(value)

    fun getChangeSubscrList() = preferencesDataSource.getChangeSubscrList()
    fun setChangeSubscrList(value: Boolean) = preferencesDataSource.setChangeSubscrList(value)

    fun getChangeAddress() = preferencesDataSource.getChangeAddress()
    fun setChangeAddress(value: Boolean) = preferencesDataSource.setChangeAddress(value)


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
    fun getSession(user: String, pass: String): Call<MainResponse> = remoteDataSource.getSession(user, pass)
    fun loadDeliveryBranches(sessionId: String): Call<MainResponse> = remoteDataSource.loadDeliveryBranches(sessionId)
    fun loadAgentInfo(sessionId: String): Call<MainResponse> = remoteDataSource.loadAgentInfo(sessionId)
    fun loadAgentLimit(sessionId: String): Call<MainResponse> = remoteDataSource.loadAgentLimit(sessionId)
    fun getDeliverySubscriptionForBranch(sessionId: String, branchShort: String): Call<MainDeliverySubscriptionResponse> =
        remoteDataSource.getDeliverySubscriptionForBranch(sessionId, branchShort)
    fun deleteDeliverySubscriptionForBranch(sessionId: String, branchShort: String, subscriptionId: String): Call<MainResponse> =
        remoteDataSource.deleteDeliverySubscriptionForBranch(sessionId, branchShort, subscriptionId)
    fun getInputFields(sessionId: String, branchShort: String): Call<MainResponse> = remoteDataSource.getInputFields(sessionId, branchShort)
    fun createSubscription(sessionId: String, branchShort: String, map: HashMap<String, ObjParam>): Call<MainDeliverySubscriptionResponse> =
        remoteDataSource.createSubscription(sessionId, branchShort, map)
    fun getFieldValues(sessionId: String, branchShort: String, fieldId: String): Call<MainResponse> =
        remoteDataSource.getFieldValues(sessionId, branchShort, fieldId)
    fun updateDeliverySubscriptionForBranch(sessionId: String, branchShort: String,
                                            subscriptionId: String, description: String, isActive: Int): Call<MainDeliverySubscriptionResponse> =
        remoteDataSource.updateDeliverySubscriptionForBranch(sessionId, branchShort, subscriptionId, description, isActive)
    fun getDeliveryTypesForSubscription(sessionId: String, branchShort: String, subscriptionId: String): Call<MainResponse> =
        remoteDataSource.getDeliveryTypesForSubscription(sessionId, branchShort, subscriptionId)
    fun setDeliveryAddressForSubscription(sessionId: String, branchShort: String, subscriptionId: String,
                                          address: String, delivetypeId: String, oldAddress: String?,
                                          isConfirm: String?, startHour: Int?, finishHour: Int?, timeZone: Int?): Call<MainResponse> =
        remoteDataSource.setDeliveryAddressForSubscription(sessionId, branchShort, subscriptionId,
            address, delivetypeId, oldAddress, isConfirm, startHour, finishHour, timeZone)


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