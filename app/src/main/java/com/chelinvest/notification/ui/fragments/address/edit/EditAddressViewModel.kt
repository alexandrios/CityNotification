package com.chelinvest.notification.ui.fragments.address.edit

import android.app.Application
import android.util.Log
import android.util.Patterns
import com.chelinvest.notification.BaseApplication
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.isEmailValid
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class EditAddressViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val errorLiveEvent = SingleLiveEvent<String>()
    val setDeliveAddressLiveEvent = SingleLiveEvent<String>()

    fun setEditSave(value: Boolean) {
        repository.setChangeAddress(value)
    }

    fun verifyAddress(type: String, address: String): Boolean {
        when (type) {
            Constants.EMAIL_ID -> {
                if (!isEmailValid(address)) {
                    //if (!address.contains("@")) {
                    errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_email_verify))
                    return false
                }
            }
            Constants.SMS_ID -> {
                if (!isValidPhoneNumber(address) || !address.contains("+7")) {
                    errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_sms_verify))
                    return false
                }
            }
            Constants.APP_PUSH_ID -> {
                if (address.isEmpty()) {
                    errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_push_verify))
                    return false
                }
            }
        }

        return true
    }

    private fun isValidPhoneNumber(target: String?): Boolean {
        return if (target == null || target.length < 12 || target.length > 12) {
            false
        } else {
            Patterns.PHONE.matcher(target).matches()
        }
    }

    fun verifyTimeRange(startHour: String, finishHour: String, timeZone: String): Boolean {

        val startH = startHour.toIntOrNull()
        val finishH = finishHour.toIntOrNull()
        val timeZ = timeZone.toIntOrNull()

        if (startH == null) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_start_hour_descr_settings))
            return false
        }

        if (finishH == null) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_finish_hour_descr_settings))
            return false
        }

        if (timeZ == null) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_time_zone_descr_settings))
            return false
        }

        if (startH >= finishH) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_time_start_more_then_finish_hour))
            return false
        }

        return true
    }

    // Создать (или привязать существующий) адрес (email, sms, push) к подписке
    fun setDeliveryAddressForSubscription(idSubscription: String,
                                          address: String,
                                          idDelivetype: String,
                                          oldAddress: String?,
                                          isConfirm: String?,
                                          startHour: Int?,
                                          finishHour: Int?,
                                          timeZone: Int?) {
        val sessionId = repository.getSessionId()
        Log.d(Constants.LOG_TAG, "EditAddressViewModel sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return
            Log.d(LOG_TAG, "EditAddressViewModel branchShort=$branchShort")

            var retVal = ""

            repository.setDeliveryAddressForSubscription(sessionId, branchShort, idSubscription,
                address, idDelivetype, oldAddress, isConfirm, startHour, finishHour, timeZone).enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d(LOG_TAG, "EditAddressViewModel onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(LOG_TAG,"EditAddressViewModel onResponse: sessionId=${result?.sessionId}")
                        Log.d(LOG_TAG,"EditAddressViewModel onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            }

                            retVal = if (result.result?.toInt() == 1) {  // 1 – выполнен успешно
                                "1"
                            } else {                              // 0 – выполнен с ошибкой
                                result.errorNote ?: "0"
                            }

                            setDeliveAddressLiveEvent.postValue(retVal)
                        }
                    }
                }
            })
        }
    }

}