package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.DeleteDeliverySubscriptionForBranchRequest
import com.chelinvest.notification.api.request.MainRequest
import java.lang.Exception

class DeleteDeliverySubscriptionForBranchInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: DeleteDeliverySubscriptionForBranchInteractor? = null
        fun getInstance(): DeleteDeliverySubscriptionForBranchInteractor {
            if(INSTANCE == null)
                INSTANCE = DeleteDeliverySubscriptionForBranchInteractor()
            return INSTANCE!!
        }
    }

    fun delete(context: Context,
                idSession: String,
                branchShort: String,
                idSubscription: String)= async {

        var errorNote: String? = null
        try {
            val request = MainRequest(DeleteDeliverySubscriptionForBranchRequest().apply {
                this.session_id = idSession
                this.branch_short = branchShort //"AGENT_REDUCTION2LIMIT"
                this.subscription_id = idSubscription
            })

            val response = send(context, request)

            errorNote = response.errorNote

        } catch (ex: Exception) {
            Log.wtf("DeleteDeliverySubscriptionForBranchInteractor->delete", ex.message)
            throw ex
        }

        Log.wtf("errorNote:", errorNote)
        errorNote
    }
}