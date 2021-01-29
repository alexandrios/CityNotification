package com.chelinvest.notification.ui.fragments.limit

import android.app.Application
import android.util.Log
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.api.response.OrgNameResponse
import com.chelinvest.notification.api.response.mapper.GetAgentInfoResponseMapper
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.model.OrgName
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LimitViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val errorLiveEvent = SingleLiveEvent<String>()
    val agentInfoLiveEvent = SingleLiveEvent<OrgName>()
    val agentLimitLiveEvent = SingleLiveEvent<String>()

    fun getAgentInfo() {
        val sessionId = repository.getSessionId()
        Log.d("session_id", "LimitViewModel getAgentInfo session_id=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            repository.loadAgentInfo(sessionId).enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d(Constants.LOG_TAG, "LimitViewModel getAgentInfo onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(Constants.LOG_TAG, "LimitViewModel getAgentInfo onResponse: sessionId=${result?.sessionId}")
                        Log.d(Constants.LOG_TAG, "LimitViewModel getAgentInfo onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            }

                            val orgName = GetAgentInfoResponseMapper().map(result.org_name as OrgNameResponse)
                            agentInfoLiveEvent.postValue(orgName)
                        }
                    }
                }
            })
        }
    }

    fun getAgentLimit() {
        val sessionId = repository.getSessionId()
        Log.d("session_id", "LimitViewModel getAgentLimit session_id=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            repository.loadAgentLimit(sessionId).enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d(Constants.LOG_TAG, "LimitViewModel getAgentLimit onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(Constants.LOG_TAG, "LimitViewModel getAgentLimit onResponse: sessionId=${result?.sessionId}")
                        Log.d(Constants.LOG_TAG, "LimitViewModel getAgentLimit onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            }

                            agentLimitLiveEvent.postValue(result.value)
                        }
                    }
                }
            })
        }
    }

//    fun loginOnClick() {
//        loginAgainLiveEvent.postValue(null)
//    }
}