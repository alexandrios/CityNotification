package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.GetDeliverySubscriptionForBranchRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.response.mapper.GetDeliverySubscriptionForBranchResponseMapper
import com.chelinvest.notification.api.response.obj_param_objs.GetDeliverySubscriptionForBranchResponse
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import java.lang.Exception

class GetDeliverySubscriptionForBranchInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: GetDeliverySubscriptionForBranchInteractor? = null
        fun getInstance(): GetDeliverySubscriptionForBranchInteractor {
            if(INSTANCE == null)
                INSTANCE = GetDeliverySubscriptionForBranchInteractor()
            return INSTANCE!!
        }
    }

    fun loadDeliverySubscriptionForBranch(context: Context, idSession: String, branchShort: String) = async {
        val objParamObjsList = ArrayList<DeliveSubscriptionForBranch>()
        try {
            val request = MainRequest(GetDeliverySubscriptionForBranchRequest().apply {
                this.session_id = idSession
                this.branch_short = branchShort //"AGENT_REDUCTION2LIMIT"
            })

            //---val response = send(context, request)
            // TODO проверить: нужно ли ещё это
            val response = send(context, request) {body: String? ->
                val res = body?.replace("obj_param_objs", "obj_subscription_objs")
                res
            }

            val mapper = GetDeliverySubscriptionForBranchResponseMapper()
            response.elements?.forEach { element ->
                mapper.map(element as GetDeliverySubscriptionForBranchResponse)?.let { element2 ->
                    objParamObjsList.add(element2)
                }
            }
        } catch (ex: Exception) {
            Log.wtf("loadDeliverySubscriptionForBranch", ex.message)
            throw ex
        }

        objParamObjsList
    }
}