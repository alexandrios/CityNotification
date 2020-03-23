package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.GetDeliveryTypesForBranchRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.response.DelivetypeExpSortResponse
import com.chelinvest.notification.api.response.mapper.DelivetypeExpSortResponseMapper
import com.chelinvest.notification.model.DelivetypeExpSort
import java.lang.Exception

class GetDeliveryTypesForBranchInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: GetDeliveryTypesForBranchInteractor? = null
        fun getInstance(): GetDeliveryTypesForBranchInteractor {
            if(INSTANCE == null)
                INSTANCE = GetDeliveryTypesForBranchInteractor()
            return INSTANCE!!
        }
    }

    fun loadDeliveryTypesForBranch(context: Context, session_id: String, branchShort: String) = async {
        val objParamObjsList = ArrayList<DelivetypeExpSort>()
        val mapper = DelivetypeExpSortResponseMapper()

        try {
            val request = MainRequest(GetDeliveryTypesForBranchRequest().apply {
                this.session_id = session_id
                this.branch_short = branchShort //"AGENT_REDUCTION2LIMIT"
            })

            val response = send(context, request)

            response.elements?.forEach { element ->
                mapper.map(element as DelivetypeExpSortResponse)?.let { element2 ->
                    objParamObjsList.add(element2)
                }
            }
        } catch (ex: Exception) {
            Log.wtf("loadDeliveryTypesForBranches", ex.message)
        }

        objParamObjsList
    }
}