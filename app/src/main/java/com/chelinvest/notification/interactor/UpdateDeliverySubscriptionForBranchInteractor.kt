package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.request.UpdateDeliverySubscriptionForBranchRequest
import com.chelinvest.notification.api.response.mapper.GetDeliverySubscriptionForBranchResponseMapper
import com.chelinvest.notification.api.response.obj_param_objs.GetDeliverySubscriptionForBranchResponse
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import java.lang.Exception
import javax.xml.stream.XMLStreamException

class UpdateDeliverySubscriptionForBranchInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: UpdateDeliverySubscriptionForBranchInteractor? = null
        fun getInstance(): UpdateDeliverySubscriptionForBranchInteractor {
            if(INSTANCE == null)
                INSTANCE = UpdateDeliverySubscriptionForBranchInteractor()
            return INSTANCE!!
        }
    }

    fun update(context: Context,
                idSession: String,
                branchShort: String,
                idSubscription: String,
                description: String,
                isActive: Int)= async {
        val objParamObjsList = ArrayList<DeliveSubscriptionForBranch>()
        var errorMess: String? = null
        try {
            val request = MainRequest(UpdateDeliverySubscriptionForBranchRequest().apply {
                this.session_id = idSession
                this.branch_short = branchShort //"AGENT_REDUCTION2LIMIT"
                this.subscription_id = idSubscription
                this.description = description
                this.is_active = isActive
            })

            //val response = send(context, request)
            // TODO проверить: нужно ли ещё это
            val response = send(context, request) { body: String? ->
                val res = body?.replace("obj_param_objs", "obj_subscription_objs")
                res
            }

            if (!response.errorNote.isNullOrEmpty()) {
                Log.wtf("UpdateDeliverySubscriptionForBranchInteractor->update", response.errorNote)
                errorMess = response.errorNote
            }

            val mapper = GetDeliverySubscriptionForBranchResponseMapper()
            response.elements?.forEach { element ->
                mapper.map(element as GetDeliverySubscriptionForBranchResponse)?.let { element2 ->
                    objParamObjsList.add(element2)
                }
            }
        } catch (ex: Exception) {
            Log.wtf("UpdateDeliverySubscriptionForBranchInteractor->update", ex.message)
            throw ex
        }

        if (!errorMess.isNullOrEmpty()) {
            throw XMLStreamException(errorMess)
        }

        objParamObjsList
    }
}