package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.*
import com.chelinvest.notification.model.ObjParam
import java.lang.Exception
import java.util.HashMap

class CreateDeliverySubscriptionForBranchInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: CreateDeliverySubscriptionForBranchInteractor? = null
        fun getInstance(): CreateDeliverySubscriptionForBranchInteractor {
            if(INSTANCE == null)
                INSTANCE = CreateDeliverySubscriptionForBranchInteractor()
            return INSTANCE!!
        }
    }

    fun create(context: Context, idSession: String, branchShort: String, map: HashMap<String, ObjParam>)= async {
        var errorNote: String? = null
        try {
            val request = MainRequest(CreateDeliverySubscriptionForBranchRequest().apply {
                this.session_id = idSession
                this.branch_short = branchShort

/* если @field:ElementArray(name="field_names_list", entry="field_name")
                this.field_names_list = Array(map.entries.size, {""})
                this.field_values_list = Array(map.entries.size, {""})
                var i = 0
                for (item in map.entries) {
                    val fieldName = item.key
                    this.field_names_list!!.set(i, fieldName)

                    val fieldValue = item.value.value
                    this.field_values_list!!.set(i, fieldValue)
                    i++
                }
*/
                this.field_names_list = ArrayList()
                this.field_values_list = ArrayList()

                for (item in map.entries) {
                    val fieldName = item.key
                    (this.field_names_list as ArrayList<String>).add(fieldName)

                    val fieldValue = item.value.value
                    (this.field_values_list as ArrayList<String>).add(fieldValue)
                }
            })

            //val response = send(context, request)
            // TODO проверить: нужно ли ещё это
            val response = send(context, request) {body: String? ->
                val res = body?.replace("obj_param_objs", "obj_subscription_objs")
                res
            }

            errorNote = response.errorNote

            /*
            val mapper = GetDeliverySubscriptionForBranchResponseMapper()
            response.elements?.forEach { element ->
                mapper.map(element as GetDeliverySubscriptionForBranchResponse)?.let { element2 ->
                    objParamObjsList.add(element2)
                }
            }
            Log.wtf("CreateDeliverySubscriptionForBranchInteractor->create", response.errorNote)
            */

        } catch (ex: Exception) {
            Log.wtf("CreateDeliverySubscriptionForBranchInteractor->create", ex.message)
        }

        //objParamObjsList
        Log.wtf("errorNote:", errorNote)
        errorNote
    }
}