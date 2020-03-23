package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.request.SetDeliveryAddressRequest
import java.lang.Exception

class SetDeliveryAddressInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: SetDeliveryAddressInteractor? = null
        fun getInstance(): SetDeliveryAddressInteractor {
            if(INSTANCE == null)
                INSTANCE = SetDeliveryAddressInteractor()
            return INSTANCE!!
        }
    }

    // Выполнить команду 1.8. set_delivery_address_for_subscription
    fun setAddress(context: Context,
                   idSession: String,
                   branchShort: String,
                   idSubscription: String,
                   address: String,
                   delivetypeId: String,
                   oldAddress: String?,
                   isConfirm: String?,
                   startHour: Int?,
                   finishHour: Int?,
                   timeZone: Int?)
            = async {
        var result = ""
        try {
            val request = MainRequest(SetDeliveryAddressRequest().apply {
                this.session_id = idSession
                this.branch_short = branchShort
                this.subscription_id = idSubscription
                this.address = address
                this.delivetype_id = delivetypeId
                this.old_address = oldAddress
                this.is_confirm = isConfirm
                this.start_hour = startHour
                this.finish_hour = finishHour
                this.timezone = timeZone
            })

            val response = send(context, request)

            if (response.result?.toInt() == 1) {  // 1 – выполнен успешно
                result = "1"
            } else {                              // 0 – выполнен с ошибкой
                result = response.errorNote ?: "0"
            }

        } catch (ex: Exception) {
            Log.wtf("setAddress", ex.message)
        }

        result
    }
}