package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.GetDeliveryBranchRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.response.ObjParamResponse
import com.chelinvest.notification.api.response.mapper.GetDeliveryBranchResponseMapper
import com.chelinvest.notification.api.response.mapper.ObjParamResponseMapper
import com.chelinvest.notification.api.response.obj_param_objs.GetDeliveryBranchResponse
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.model.ObjParamObjs
import java.lang.Exception

class GetDeliveryBranchInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: GetDeliveryBranchInteractor? = null
        fun getInstance(): GetDeliveryBranchInteractor {
            if(INSTANCE == null)
                INSTANCE = GetDeliveryBranchInteractor()
            return INSTANCE!!
        }
    }

    // при result_type = X_OBJ_PARAM (только список типов уведомлений)
    fun loadDeliveryBranches(context: Context, session_id: String) = async {
        val objParamList = ArrayList<ObjParam>()
        try {
            val request = MainRequest(GetDeliveryBranchRequest().apply {
                this.session_id = session_id
            })

            val response = send(context, request)

            val mapper = ObjParamResponseMapper()

            response.elements?.forEach { element ->
                mapper.map(element as ObjParamResponse)?.let { element2 ->
                    objParamList.add(element2)
                }
            }
        } catch (ex: Exception) {
            Log.wtf("loadDeliveryBranches", ex.message)
        }

        objParamList
    }

    // при result_type = X_OBJ_PARAM_OBJS – с указанием типов рассылок для каждого типа уведомлений
    fun loadDeliveryBranchesWithAllows(context: Context, session_id: String) = async {
        val objParamObjsList = ArrayList<ObjParamObjs>()
        try {
            val request = MainRequest(GetDeliveryBranchRequest().apply {
                this.session_id = session_id
            })

            //---val response = send(context, request)
            // TODO проверить: нужно ли ещё это
            val response = send(context, request) {body: String? ->
                val res = body?.replace("obj_param_objs", "obj_delivetype_objs")
                res
            }

            val mapper = GetDeliveryBranchResponseMapper()

            response.elements?.forEach { element ->
                mapper.map(element as GetDeliveryBranchResponse)?.let { element2 ->
                    objParamObjsList.add(element2)
                }
            }
        } catch (ex: Exception) {
            Log.wtf("loadDeliveryBranchesWithAllows", ex.message)
        }

        objParamObjsList
    }
}