package com.chelinvest.notification.ui.fragments.branch

import android.app.Application
import android.util.Log
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.api.response.ObjParamResponse
import com.chelinvest.notification.api.response.mapper.ObjParamResponseMapper
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

// при result_type = X_OBJ_PARAM (только список типов уведомлений)
// при result_type = X_OBJ_PARAM_OBJS – с указанием типов рассылок для каждого типа уведомлений

class BranchViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val errorLiveEvent = SingleLiveEvent<String>()
    val branchesLiveEvent = SingleLiveEvent<ArrayList<ObjParam>>()
//    val loginAgainLiveEvent = SingleLiveEvent<Nothing>()

    fun saveBranchShort(value: String) {
        repository.setBranchShort(value)
    }

    fun getDelivetypeExp() {
        val sessionId = repository.getSessionId()
        Log.d(Constants.LOG_TAG, "BranchViewModel sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            //view.showProgressDialog()
            val objParamList = ArrayList<ObjParam>()
            //val response = GetDeliveryBranchInteractor.getInstance().loadDeliveryBranches(context, sessionId).await()
            repository.loadDeliveryBranches(sessionId).enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d(Constants.LOG_TAG, "BranchViewModel onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(Constants.LOG_TAG, "BranchViewModel onResponse: sessionId=${result?.sessionId}")
                        Log.d(Constants.LOG_TAG, "BranchViewModel onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            }

                            val mapper = ObjParamResponseMapper()

                            result.elements?.forEach { element ->
                                mapper.map(element as ObjParamResponse)?.let { element2 ->
                                    objParamList.add(element2)
                                }
                            }

                            branchesLiveEvent.postValue(objParamList)
                            //view.hideProgressDialog()
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