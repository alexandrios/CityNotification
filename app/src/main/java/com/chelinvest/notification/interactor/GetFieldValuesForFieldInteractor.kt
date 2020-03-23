package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.GetFieldValuesForFieldRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.response.ObjParamResponse
import com.chelinvest.notification.api.response.mapper.ObjParamResponseMapper
import com.chelinvest.notification.model.ObjParam
import java.lang.Exception

class GetFieldValuesForFieldInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: GetFieldValuesForFieldInteractor? = null
        fun getInstance(): GetFieldValuesForFieldInteractor {
            if(INSTANCE == null)
                INSTANCE = GetFieldValuesForFieldInteractor()
            return INSTANCE!!
        }
    }

    fun loadValuesForField(context: Context, idSession: String, branchShort: String, idField: String) = async {
        val objParamList = ArrayList<ObjParam>()
        try {
            val request = MainRequest(GetFieldValuesForFieldRequest().apply {
                this.session_id = idSession
                this.branch_short = branchShort
                this.field_id = idField
            })

            val response = send(context, request)

            val mapper = ObjParamResponseMapper()

            response.elements?.forEach { element ->
                mapper.map(element as ObjParamResponse)?.let { element2 ->
                    objParamList.add(element2)
                }
            }
        } catch (ex: Exception) {
            Log.wtf("loadValuesForField", ex.message)
        }

        objParamList
    }

}