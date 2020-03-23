package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.CheckAgentLimitRequest
import com.chelinvest.notification.api.request.MainRequest
import java.lang.Exception

class CheckAgentLimitInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: CheckAgentLimitInteractor? = null
        fun getInstance(): CheckAgentLimitInteractor {
            if(INSTANCE == null)
                INSTANCE = CheckAgentLimitInteractor()
            return INSTANCE!!
        }
    }

    fun loadAgentLimit(context: Context, session_id: String) = async {
        var value: String? = null
        try {
            val request = MainRequest(CheckAgentLimitRequest().apply {
                this.session_id = session_id
            })

            val response = send(context, request)
            value = response.value

        } catch (ex: Exception) {
            Log.wtf("loadAgentLimit", ex.message)
        }

        value
    }

}