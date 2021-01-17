package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import com.chelinvest.notification.additional.async
import com.chelinvest.notification.api.request.GetInputFieldsForBranchRequest
import com.chelinvest.notification.api.request.LoginRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.model.OrgName
import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.ui.login.ResultTypeAgentInfo

class LoginInteractor private constructor(): Interactor() {

    companion object {
        private var INSTANCE: LoginInteractor? = null
        fun getInstance(): LoginInteractor {
            if (INSTANCE == null)
                INSTANCE = LoginInteractor()
            return INSTANCE!!
        }
    }

    fun loginByPasswordAsync(/*context: Context,*/ user: String, pass: String) = async {
        val session = Session()
        try {
            val request = MainRequest(LoginRequest().apply {
                this.user = user
                this.password = pass
            })

            val response = send(/*context,*/ request)
            session.setResponse(response)

        } catch (ex: Exception) {
            Log.d("loginByPasswordAsync", ex.message.toString())
            throw ex
        }
        session
    }


    fun getAgentInfoAsync(context: Context, idSession: String, resultType: ResultTypeAgentInfo) = async {

        val agentInfo = OrgName()

        try {
            val request = MainRequest(GetInputFieldsForBranchRequest().apply {
                this.session_id = idSession
                when (resultType) {
                    ResultTypeAgentInfo.X_ORG_ID -> this.resultType = "X_ORG_ID"
                    ResultTypeAgentInfo.X_ORG_NAME ->  this.resultType = "X_ORG_NAME"
                    else -> this.resultType = "X_ORG_ID"
                }
            })

            val response = send(/*context,*/ request)
            // TODO: проверить response.errorNote

            if (response.org_name != null) {
                agentInfo.id = response.org_name!!.id
                agentInfo.name = response.org_name!!.name
            }

        } catch (ex: Exception) {
            Log.wtf("getAgentInfoAsync", ex.message)
        }

        agentInfo
    }

}