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
import com.chelinvest.notification.utils.Constants.APP_PUSH_ID
import com.chelinvest.notification.utils.Constants.EMAIL_ID
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.Constants.SMS_ID
import com.chelinvest.notification.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

class EditAddressViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val errorLiveEvent = SingleLiveEvent<String>()
    val setDeliveAddressLiveEvent = SingleLiveEvent<String>()

    fun setEditSave(value: Boolean) {
        repository.setChangeAddress(value)
    }

    fun getFCMToken(): String? {
        return repository.getFCMToken()
    }

    private fun getPreferTimeZoneMap(hour: Int): String? {
        return repository.getPreferTimeZone(hour)
    }

    fun setPreferTimeZoneMap(hour: Int, value: String) {
        repository.setPreferTimeZone(hour, value)
    }

    fun verifyAddress(type: String, address: String): Boolean {
        when (type) {
            EMAIL_ID -> {
                if (!isEmailValid(address)) {
                    //if (!address.contains("@")) {
                    errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_email_verify))
                    return false
                }
            }
            SMS_ID -> {
                if (!isValidPhoneNumber(address) || !address.contains("+7")) {
                    errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_sms_verify))
                    return false
                }
            }
            APP_PUSH_ID -> {
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
    fun setDeliveryAddressForSubscription(
        idSubscription: String,
        address: String,
        idDelivetype: String,
        oldAddress: String?,
        isConfirm: String?,
        startHour: Int?,
        finishHour: Int?,
        timeZone: Int?
    ) {
        val sessionId = repository.getSessionId()
        Log.d(Constants.LOG_TAG, "EditAddressViewModel sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return
            Log.d(LOG_TAG, "EditAddressViewModel branchShort=$branchShort")

            var retVal = ""

            repository.setDeliveryAddressForSubscription(
                sessionId, branchShort, idSubscription,
                address, idDelivetype, oldAddress, isConfirm, startHour, finishHour, timeZone
            ).enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d(LOG_TAG, "EditAddressViewModel onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(
                    call: Call<MainResponse>,
                    response: Response<MainResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(
                            LOG_TAG,
                            "EditAddressViewModel onResponse: sessionId=${result?.sessionId}"
                        )
                        Log.d(
                            LOG_TAG,
                            "EditAddressViewModel onResponse: errorNote=${result?.errorNote}"
                        )

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

    fun checkHourRange(text: String?) {
        val hour = text?.toIntOrNull()
        hour?.let {
            if (it < 0 || it > 23) {
                errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_time_check_hour_range))
            }
        }
    }

    fun checkTimeZone(text: String?) {
        val hour = text?.toIntOrNull()
        hour?.let {
            if (it < -11 || it > 12) {
                errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.edit_time_check_timezone_range))
            }
        }
    }

    // Получить Map вида: <"(UTC + 05:00) Екатеринбург", 18000000> часовых поясов
    fun getTimeZone(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        val ids: Array<String> = TimeZone.getAvailableIDs()
        Log.d(LOG_TAG, ids.size.toString())
        for (id in ids) {
            val d = TimeZone.getTimeZone(id)
            var region: String = d.getDisplayName(Locale("ru","RU"))
            // Убрать GMT и Катманду и иже с ним (полчаса, 45 минут...)
            if (region.substring(0, 3).toUpperCase(Locale.ROOT) != "GMT" &&
                    d.rawOffset.rem(3600000) == 0) {
                if (region.contains(", стандартное время")) {
                    region = region.substring(0, region.length - ", стандартное время".length)
                }
                val hours = abs(d.rawOffset) / 3600000
                val minutes = abs(d.rawOffset / 60000) % 60
                val sign = if (d.rawOffset >= 0) "+" else "-"
                val timeZonePretty = String.format("(UTC %s %02d:%02d) %s", sign, hours, minutes, region)

                map[timeZonePretty] = d.rawOffset
            }
        }

        val map2 = map.toList().sortedBy { (_, value) -> value }.toMap()
        /*
        for (pair in map2) {
            Log.d(LOG_TAG, "${pair.value} ${pair.key}")
        }*/
        Log.d(LOG_TAG, map2.size.toString())
        return map2
    }

    // Получить позицию в Map часовых поясов по указанному часу (hour)
    fun getTimeZonePosition(map: Map<String, Int>, hour: Int): Int {
        // Поскольку в map может быть несколько строк с одним и тем же часом,
        // на устройстве храним предпочтения, например +5 = Екатеринбург, +3 = Москва
        val nameTimeZone = getPreferTimeZoneMap(hour)
        var position = if (nameTimeZone != null) {
            // Если есть предпочтения - искать их в списке
            map.keys.indexOf(nameTimeZone)
        } else {
            // Если предпочтений нет - искать первый подходящий в списке
            map.values.toList().indexOf( hour * 3600000)
        }
        if (position == -1) position = 0
        return position
    }

}