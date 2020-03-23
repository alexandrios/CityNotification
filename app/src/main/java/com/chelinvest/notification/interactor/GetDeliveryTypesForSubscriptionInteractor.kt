package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.GetDeliveryTypesForSubscriptionRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.response.DelivetypeAddrsResponse
import com.chelinvest.notification.api.response.mapper.DelivetypeAddrsResponseMapper
import com.chelinvest.notification.model.DelivetypeAddrs
import java.lang.Exception

class GetDeliveryTypesForSubscriptionInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: GetDeliveryTypesForSubscriptionInteractor? = null
        fun getInstance(): GetDeliveryTypesForSubscriptionInteractor {
            if(INSTANCE == null)
                INSTANCE = GetDeliveryTypesForSubscriptionInteractor()
            return INSTANCE!!
        }
    }

    fun loadDeliveryTypesForSubscription(context: Context, idSession: String, branchShort: String, idSubscription: String) = async {
        val objParamObjsList = ArrayList<DelivetypeAddrs>()
        val mapper = DelivetypeAddrsResponseMapper()

        try {
            val request = MainRequest(GetDeliveryTypesForSubscriptionRequest().apply {
                this.session_id = idSession
                this.branch_short = branchShort
                this.subscription_id = idSubscription
            })

            val response = send(context, request)

            // Обработать полученный список: убрать не свои токены для APP_PUSH
            val token = Preferences.getInstance().getFCMToken(context) ?: ""

            response.elements?.forEach { element ->
                mapper.map(token, element as DelivetypeAddrsResponse)?.let { element2 ->
                    objParamObjsList.add(element2)
                }
            }
        } catch (ex: Exception) {
            Log.wtf("loadDeliveryTypesForSubscription", ex.message)
        }

        objParamObjsList
    }
}