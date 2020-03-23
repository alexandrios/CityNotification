package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.GetAgentInfoRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.response.OrgNameResponse
import com.chelinvest.notification.api.response.mapper.GetAgentInfoResponseMapper
import com.chelinvest.notification.model.OrgName
import java.lang.Exception

class GetAgentInfoInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: GetAgentInfoInteractor? = null
        fun getInstance(): GetAgentInfoInteractor {
            if(INSTANCE == null)
                INSTANCE = GetAgentInfoInteractor()
            return INSTANCE!!
        }
    }

    fun loadAgentInfo(context: Context, session_id: String) = async {
        var orgName: OrgName? = null
        try {
            val request = MainRequest(GetAgentInfoRequest().apply {
                this.session_id = session_id
            })

            val response = send(context, request)
            orgName = GetAgentInfoResponseMapper().map(response.org_name as OrgNameResponse)

        } catch (ex: Exception) {
            Log.wtf("loadAgentInfo", ex.message)
        }

        orgName
    }

}