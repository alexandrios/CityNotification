package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.GetInputFieldsForBranchRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.response.ObjAnyResponse
import com.chelinvest.notification.api.response.mapper.ObjAnyResponseMapper
import com.chelinvest.notification.model.ObjAny
import java.lang.Exception

class GetInputFieldsForBranchInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: GetInputFieldsForBranchInteractor? = null
        fun getInstance(): GetInputFieldsForBranchInteractor {
            if(INSTANCE == null)
                INSTANCE = GetInputFieldsForBranchInteractor()
            return INSTANCE!!
        }
    }

    fun loadFieldsForBranch(context: Context, idSession: String, branchShort: String) = async {
        val objAnyList = ArrayList<ObjAny>()
        try {
            val request = MainRequest(GetInputFieldsForBranchRequest().apply {
                this.session_id = idSession
                this.branch_short = branchShort
            })

            val response = send(context, request)

            val mapper = ObjAnyResponseMapper()

            response.elements?.forEach { element ->
                mapper.map(element as ObjAnyResponse)?.let { element2 ->
                    objAnyList.add(element2)
                }
            }
        } catch (ex: Exception) {
            Log.wtf("loadFieldsForBranch", ex.message)
        }

        objAnyList
    }

}